package com.ecommerce.project.controller;

import com.ecommerce.project.model.AppRoleName;
import com.ecommerce.project.model.Role;
import com.ecommerce.project.model.User;
import com.ecommerce.project.repositories.UserRepository;
import com.ecommerce.project.security.jwt.JwtUtils;
import com.ecommerce.project.security.request.LoginRequest;
import com.ecommerce.project.security.request.SignupRequest;
import com.ecommerce.project.security.response.LoginResponse;
import com.ecommerce.project.security.response.MessageResponse;
import com.ecommerce.project.security.services.UserDetailsImp;
import com.ecommerce.project.repositories.RoleRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired // Spring Providing its Implementation
    //We have created its Bean inside SpringConfig using @Bean....
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils; //DI-for JwtToken creation

    @Autowired
    //Directly adding userRepo in Controller for Easyness.
    //Its better to have AuthService for Industry Grade Appl.
    UserRepository userRepository; //For SignUP Duplication check

    @Autowired //Used in SignUp - To store Passwd of user as Encrypted
    PasswordEncoder passwordEncoder;

    @Autowired
    //Used in Sing up
    //Used to Check whether particular Role Exists in DB or not Before assigning to user.
    RoleRepository roleRepository;

    @PostMapping(value = "/signin")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        /*
                        > SIGN-IN FLOW STARTS HERE <
           User has sent - JSON Object with userName & Password
           IMPORTANT: This is DIFFERENT from JwtAuthFilter flow.
           HERE: -> User is logging in FIRST TIME -> Username + Password authentication happens
           In JwtFilter: -> User already logged in -> JWT token validation happens
        */
        Authentication authenticationObject;

        /*
                UsernamePasswordAuthenticationToken => Implementation of Authentication interface.

                RIGHT NOW: This object is ONLY an authentication REQUEST.
                It currently contains:
                principal   -> username
                credentials -> password
                authorities -> empty

                $$$ User is NOT authenticated yet! $$$
         */
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUserName(),
                        loginRequest.getPassword()
                );
        /*
                Below IS THE REAL AUTHENTICATION STEP

                **IMPORTANT! below method only used in Login flow and Never in JWTAuthFilter**
                **Because it checks for username+passwd, while in JWTAuthFilter we check JwtToken**

                authenticationManager.authenticate(...)
                internally triggers:

                AuthenticationManager
                        ↓
                ProviderManager
                        ↓
                DaoAuthenticationProvider
                        ↓
                UserDetailsService.loadUserByUsername() #IMPORTANT#
                        ↓
                Fetch user from DB
                        ↓
                PasswordEncoder.matches(raw, encoded)
                        ↓
                Authentication Success/Failure

                ## IMPORTANT ##
                ## Roles/Authorities are ALSO fetched from DB during loadUserByUsername() ##
             */
        try {
            authenticationObject =
                    authenticationManager.authenticate(
                            usernamePasswordAuthenticationToken
                    );

            System.out.println("**> authentication object created");
            /*
                If authentication succeeds: authenticationObject now becomes FULLY AUTHENTICATED object containing:

                -> principal (UserDetails)
                -> authorities/roles
                -> authenticated = true

                If authentication fails: authenticate() throws AuthenticationException.

                Possible reasons: -> Wrong password -> User not found -> Account disabled -> Credentials expired
             */
        } catch (AuthenticationException e) {
            System.out.println("** Authentication Exception Raised");
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad Credentials");
            map.put("status", false);

            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED); //401
        }

        //--> Reached Till Here: Means-Authentication is Successful
        //Setting authenticationObject into SpringContext
        SecurityContextHolder.getContext().setAuthentication(authenticationObject);

        //Generate a JWTToken to handOver it to User

        //UserDetails is required for -> generateTokenFromUserName(UserDetails)
        UserDetailsImp userDetailsImp = (UserDetailsImp) authenticationObject.getPrincipal();
        String jwtToken = jwtUtils.generateJwtTokenFromUserName(userDetailsImp);

        //Getting all roles as list
        List<String> roles = userDetailsImp
                .getAuthorities()
                .stream()
                .map(x -> x.getAuthority())
                .toList();

        String userName = loginRequest.getUserName();
        Long userId = userDetailsImp.getId();

        //Preparing What to return after success of Login
        LoginResponse loginResponse =
                new LoginResponse(userId, jwtToken, userName, roles);

        return ResponseEntity.ok(loginResponse);
    }


    @PostMapping("/signup")
    public ResponseEntity<?> singUpUser(@RequestBody @Valid SignupRequest signupRequest) {

        //Check for Duplication of userName
        if (userRepository.existsByUserName(signupRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        //Check for Duplication of email
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already taken!"));
        }

        //Creating object of Entity/Model User to store in Database
        User userEntity = new User();
        userEntity.setUserName(signupRequest.getUsername());
        userEntity.setEmail(signupRequest.getEmail());
        userEntity.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        //In Json of "SignupRequest" roles will be --> List of Strings
        Set<String> inputRoles = signupRequest.getRole();

        //To store it in UserEntity we need Set<Role>
        //Where Role is Entity-Model having roleName as Enum-AppRoleName
        Set<Role> rolesSet = new HashSet<>();

        //User has sent No Roles information
        if (inputRoles == null || inputRoles.isEmpty()) {
            Role userRole = roleRepository.findByRoleName(AppRoleName.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role Not Found in Database!!!"));
            rolesSet.add(userRole);

        } else {

            //Traverse Each Role String and Check does that RoleName Exists by Name in Database or Not
            inputRoles.forEach(role -> {
                switch (role) {

                    case "admin":
                        Role adminRole = roleRepository.findByRoleName(AppRoleName.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role Not Found in Database!!!"));
                        rolesSet.add(adminRole);
                        break;

                    case "seller":
                        Role sellerRole = roleRepository.findByRoleName(AppRoleName.ROLE_SELLER)
                                .orElseThrow(() -> new RuntimeException("Error: Role Not Found in Database!!!"));
                        rolesSet.add(sellerRole);

                    // -> If garbage role is provided By User That makes no sense Assign USER to it
                    default:
                        Role userRole = roleRepository.findByRoleName(AppRoleName.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role Not Found in Database!!!"));
                        rolesSet.add(userRole);
                }
            });
        }

        userEntity.setRoles(rolesSet);
        userRepository.save(userEntity);
        return ResponseEntity.ok(new MessageResponse("User Registered Successfully!!!"));

    }
}
