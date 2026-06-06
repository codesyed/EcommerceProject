//package com.example.securitydemo;
//
//import com.example.jwt.JwtUtils;
//import com.example.jwt.LoginRequest;
//import com.example.jwt.LoginResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//public class GreetingController {
//
//    @Autowired //From where we are getting its implementation?
//    private AuthenticationManager authenticationManagerDependency;
//
//    @Autowired
//    private JwtUtils jwtUtils; //DI-for JwtToken creation
//
//    @PreAuthorize("hasRole('USER')")
//    @GetMapping("/user")
//    public String sayHello(){
//        return "Hello-User";
//    }
//
//    @GetMapping("/public")
//    public String sayHello2(){
//        return "Hello-Public!";
//    }
//
//    @PreAuthorize("hasRole('ADMIN')")
//    @GetMapping("/admin")
//    public String sayHell3(){
//        return "Hello-Admin!";
//    }
//
//    @PostMapping("/signin")
//    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
//            /*  SIGN-IN FLOW STARTS HERE
//                User has sent: JSON Object with userName & Passwd
//
//                IMPORTANT: This is DIFFERENT from JwtAuthFilter flow.
//                HERE: -> User is logging in FIRST TIME -> Username + Password authentication happens
//
//                In JwtFilter: -> User already logged in -> JWT token validation happens
//             */
//        Authentication authenticationObject;
//
//        try{
//            /*
//                UsernamePasswordAuthenticationToken = Implementation of Authentication interface.
//
//                RIGHT NOW: This object is ONLY an authentication REQUEST.
//                It currently contains:
//
//                principal   -> username
//                credentials -> password
//                authorities -> empty
//
//                % User is NOT authenticated yet.%
//             */
//            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
//                    new UsernamePasswordAuthenticationToken(
//                            loginRequest.getUserName(),
//                            loginRequest.getPassword()
//                    );
//
//            /*
//                Below IS THE REAL AUTHENTICATION STEP
//
//                **IMPORTANT! below method only used in Login flow and Never in JWTAuthFilter**
//                **Because it checks for username+passwd, while in JWTAuthFilter we check JwtToken**
//
//                authenticationManager.authenticate(...)
//                internally triggers:
//
//                AuthenticationManager
//                        ↓
//                ProviderManager
//                        ↓
//                DaoAuthenticationProvider
//                        ↓
//                UserDetailsService.loadUserByUsername() #IMPORTANT#
//                        ↓
//                Fetch user from DB
//                        ↓
//                PasswordEncoder.matches(raw, encoded)
//                        ↓
//                Authentication Success/Failure
//
//                ## IMPORTANT ##
//                ## Roles/Authorities are ALSO fetched from DB during loadUserByUsername() ##
//             */
//
//            authenticationObject =
//                    authenticationManagerDependency.authenticate(
//                            usernamePasswordAuthenticationToken
//                    );
//            /*
//                If authentication succeeds: authenticationObject now becomes FULLY AUTHENTICATED object containing:
//
//                -> principal (UserDetails)
//                -> authorities/roles
//                -> authenticated = true
//
//                If authentication fails: authenticate() throws AuthenticationException.
//
//                Possible reasons: -> Wrong password -> User not found -> Account disabled -> Credentials expired
//             */
//        }
//        catch (AuthenticationException e){
//            Map<String, Object> map = new HashMap<>();
//            map.put("message", "Bad Credentials");
//            map.put("status", false);
//
//            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED); //401
//        }
//
//        //--> Reached Till Here: Means-Authentication is Successful
//        //Setting authenticationObject into SpringContext
//        SecurityContextHolder.getContext().setAuthentication(authenticationObject);
//
//        //Generate a JWTToken to handOver it to User
//
//        //UserDetails is required for generateTokenFromUserName(UserDetails)
//        UserDetails userDetails = (UserDetails) authenticationObject.getPrincipal();
//        String jwtToken  = jwtUtils.generateJwtTokenFromUserName(userDetails);
//
//        //Getting all roles as list
//
//        List<String> roles = userDetails
//                .getAuthorities()
//                .stream()
//                .map(x->x.getAuthority())
//                .toList();
//
//        String userName = loginRequest.getUserName();
//
//        //Preparing What to return after success of Login
//        LoginResponse loginResponse =
//        new LoginResponse(jwtToken, userName, roles);
//
//        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
//    }
//}
