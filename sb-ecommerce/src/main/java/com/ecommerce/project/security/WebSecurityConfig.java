package com.ecommerce.project.security;

import com.ecommerce.project.security.jwt.AuthEntryPoint;
import com.ecommerce.project.security.jwt.AuthTokenFilter;
import com.ecommerce.project.security.jwt.CustomAccessDeniedHandler;
import com.ecommerce.project.security.services.UserDetailsServiceImpl;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration //To enable @Bean-Creation
@EnableWebSecurity //To Specify that this class will have 'Custom Spring Security Configuration'
@EnableMethodSecurity //To Enable WORKING OF -> @PreAuthorize() & @PostAuthorize() used inside Controller.
public class WebSecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPoint customUnAuthorizedHandler; //To Manual Handle 401

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler; //To Manual Handle 403

    @PostConstruct
    public void init(){
        System.out.println("** Check:-> SecurityConfig Execution Completed!");
    }

    @Bean //To Apply filter created By Us Before every Request Hitting to Controller
    public AuthTokenFilter authenticationJwtTokenFilter(){
        return new AuthTokenFilter();
    }


    /* 1) AuthenticationManager and AuthenticationProvider are mainly used during the login/authentication process
          to verify user credentials. */

    /* 2) After successful authentication, Spring Security typically relies on a Session or JWT token
          for subsequent requests instead of re-authenticating the username and password each time. */

    /* 3) AuthenticationManager + AuthenticationProvider Only used In SignIn/Login Request Never after that */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider =
                new DaoAuthenticationProvider(userDetailsService);

        authenticationProvider.setPasswordEncoder( givePasswordEncoder() );
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder givePasswordEncoder(){
        //BCryptPasswordEncoder is one of the implementation(child) of PasswordEncoder Interface
        //Mostly Preferred implementation of PasswordEncoder is BCryptPasswordEncoder
        return new BCryptPasswordEncoder();
    }

    @Bean
    /* 1) This bean will be used in Authenticating username +passwd in 'LOGIN' .authenticate()...*/
    /* 2) AuthenticationManager + AuthenticationProvider Only used In SignIn/Login Request Never after that */
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig){
        return authConfig.getAuthenticationManager();
    }


    /*
        WebSecurityCustomizer is used to completely bypass Spring Security for specific URLs.
        >> These URLs do not enter the Security Filter Chain at all <<
        Difference: permitAll()
                        ↓
             Request enters Security Filters but access is allowed.

                    ignoring()
                        ↓
            Request completely skips Security Filters.
     */
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web ->
                web.ignoring().requestMatchers(
                        "/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/swagger-ui.html",
                        "/webjars/**"
                ));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http){

        //Making EndPoints secure
        //.permitAll(): These endPoint doesn't require anyAuthentication when reaches here let all access it.
        //.permitAll(): Doesn't remove SecurityFilters execution just let these request to pass on...

        // No *  -> Exact match only | * -> Matches ONE path level |  **    -> Matches ANY number of path levels

        //1) Disabling CSRF
        http.csrf( csrf -> csrf.disable());

        //2) Custom Exception Handling 401 and 403
        http.exceptionHandling(exception ->
                        exception.authenticationEntryPoint(customUnAuthorizedHandler) //401
                                .accessDeniedHandler(customAccessDeniedHandler) //403
                );

        //3) Making sessionManagement to Stateless
        http.sessionManagement(
                        session ->
                                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        //4) Managing which API should be authenticated & must Not Go Inside "Custom Filter-AuthTokenFilter"
        http.authorizeHttpRequests(request ->
                        request.requestMatchers("/h2-console/**").permitAll()
                                .requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/api/public/**").permitAll()
                                .requestMatchers("/api/test/**").permitAll()
                                .requestMatchers("/images/**").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()

                                //Prevent others then ADMIN to Reach Controller of this URL
                                //It's Authorization not Authentication
                                //Actual Authorization Before Controller
                                //@PreAuthorize() --> Actual Authorization before API execution BUT inside Controller.
                                .requestMatchers("/api/admin/**")
                                .hasRole("ADMIN")

                                //It's Authentication not Authorization
                                //Just Check Correctness of JwtToken & No more.
                                .anyRequest().authenticated()

                                 /*
                                    .authenticated() performs only authentication checks (valid logged-in user).
                                    It does not perform role-based authorization,
                                    To restrict access based on roles, we must explicitly configure rules
                                        > such as .hasRole(), .hasAuthority(), or @PreAuthorize().
                                 */
                        );

        //5) Providing authentication Provider.
        http.authenticationProvider( authenticationProvider() );

        //6) Finally Placing this custom Filter (AuthTokenFilter) before UsernamePasswordAuthenticationFilter
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);


        // http.httpBasic(withDefaults()); --> No need with JWT
        // http.formLogin(withDefaults()); --> No need with JWT

        /*
            formLogin() activates Spring's default login mechanism using "UsernamePasswordAuthenticationFilter"

            In custom JWT authentication,we create our own login API, so this filter no longer handles authentication logic.

            It still exists internally in filter chain,
            but mainly acts as a filter-order reference point and does not interfere with custom JWT login flow.
        */

        http.headers(headers->headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        SecurityFilterChain customFilter = http.build();
        return customFilter;
    }



}
