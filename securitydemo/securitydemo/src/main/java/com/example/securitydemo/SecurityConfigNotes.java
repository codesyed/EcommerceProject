//package com.example.securitydemo;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.provisioning.JdbcUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//import javax.sql.DataSource;
//
//import static org.springframework.security.config.Customizer.withDefaults;
//
///*
//    =========================================================
//                    SECURITY CONFIG CLASS
//    =========================================================
//    Purpose:
//    Defines HOW Spring Security should behave
//    for incoming HTTP requests/APIs.
//
//    This class customizes:
//    - authentication
//    - authorization
//    - sessions
//    - login type
//    - security rules
//    - filters etc.
// */
//
//@Configuration
//// Enables creation of Spring Beans from this class
//
//@EnableWebSecurity
//// Specify that we are mentioning a manual Spring Configure here...
//// Enables Spring Security for HTTP Requests/APIs
//// Security works mainly at request/filter level
//
//@EnableMethodSecurity
//// Enables:
//// @PreAuthorize
//// @PostAuthorize
//// method-level security annotations
//
//public class SecurityConfigNotes {
//
//
//    /*
//        Spring Boot automatically creates DataSource Bean
//        using:
//        - pom.xml dependency
//        - application.properties DB config
//
//        We only inject/use it.
//     */
//
//    @Autowired
//    DataSource dataSource;
//
//    /*
//        =========================================================
//                SECURITY FILTER CHAIN CONFIGURATION
//        =========================================================
//
//        SecurityFilterChain:
//        Chain/list of security filters through which
//        EVERY HTTP request passes FIRST.
//
//        HttpSecurity:
//        Helper/builder object used to configure:
//        - authentication
//        - authorization
//        - sessions
//        - csrf
//        - login type
//        - JWT etc.
//     */
//
//    @Bean
//    SecurityFilterChain mySecurityConfig(HttpSecurity http)
//            throws Exception {
//        /*
//            =====================================================
//                    AUTHORIZATION RULES
//            =====================================================
//
//            requestMatchers("/h2-console/**").permitAll()
//            -> H2 console accessible without login
//
//            anyRequest().authenticated()
//            -> every other API requires authentication
//         */
//
//        http.authorizeHttpRequests(requests ->
//                requests
//                        .requestMatchers("/h2-console/**")
//                        .permitAll()
//
//                        .anyRequest()
//                        .authenticated()
//        );
//
//        /*
//            =====================================================
//                    SESSION MANAGEMENT
//            =====================================================
//            STATELESS:
//            -> Spring Security will NOT maintain session state
//
//            Means: backend forgets user after request ends.
//
//            Mostly used with:
//            - JWT
//            - REST APIs
//
//            NOTE:
//            httpBasic() can still work,
//            but credentials/token must come again in future requests.
//         */
//
//        http.sessionManagement(session ->
//                session.sessionCreationPolicy(
//                        SessionCreationPolicy.STATELESS
//                )
//        );
//        /*
//            =====================================================
//                    AUTHENTICATION TYPE
//            =====================================================
//            httpBasic()
//            Credentials shared using: Authorization Header
//            Example: Authorization: Basic base64(username:password)
//
//            Mostly used for:
//            - APIs
//            - Postman
//            - testing
//         */
//
//        http.httpBasic(withDefaults());
//
//        /*
//            formLogin() : Enables browser login form UI.
//            Credentials sent using: - form data/body parameters
//
//            Mostly used for:
//            - traditional websites
//            - browser login pages
//         */
//
////        http.formLogin(withDefaults());
//
//        /*
//            =====================================================
//                    H2 CONSOLE FIX
//            =====================================================
//
//            H2 console uses frames/iframe internally.
//            By default Spring Security blocks frames for security reasons.
//
//            sameOrigin()
//            -> allow frames only from SAME origin/domain.
//         */
//
//        http.headers(headers ->
//                headers.frameOptions(
//                        HeadersConfigurer.FrameOptionsConfig::sameOrigin
//                )
//        );
//
//        /*
//            =====================================================
//                            CSRF
//            =====================================================
//
//            CSRF:
//            Browser-session based attack protection.
//
//            Usually disabled for:
//            - REST APIs
//            - JWT APIs
//            - stateless authentication
//         */
//
//        http.csrf(AbstractHttpConfigurer::disable);
//
//        /*
//            build()
//            -> Creates final SecurityFilterChain object
//            using all configured rules/settings.
//         */
//
//        return http.build();
//    }
//
//
//    /*
//        =========================================================
//                        USER MANAGEMENT
//        =========================================================
//
//        UserDetails
//        -> actual authenticated user information - means near to actual user object
//
//        UserDetailsService
//        -> loads/fetches user information from - Database to code and vice versa
//
//        InMemoryUserDetailsManager (implementation of UserDetailsService)
//        -> manages users from application memory
//
//        JdbcUserDetailsManager (implementation of UserDetailsService)
//        -> manages users using DATABASE
//     */
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//
//        /*
//            =====================================================
//                        NORMAL USER
//            =====================================================
//         */
//
//        UserDetails user = User.withUsername("syed")
//
//                // {noop}
//                // -> no password hashing/encoding
//                // -> only for learning/testing
//
//                .password("{noop}syed")
//
//                .roles("USER")
//
//                .build();
//
//
//        /*
//            =====================================================
//                        ADMIN USER
//            =====================================================
//         */
//
//        UserDetails admin = User.withUsername("admin")
//
//                .password("{noop}admin")
//
//                .roles("ADMIN")
//
//                .build();
//        /*
//            =====================================================
//                JDBC USER DETAILS MANAGER
//            =====================================================
//
//            JdbcUserDetailsManager:
//            implementation of UserDetailsService
//
//            Stores/manages users in DATABASE
//            using DataSource.
//
//            Can:
//            - create users
//            - load users
//            - authenticate users
//         */
//
//        JdbcUserDetailsManager userDetailsManager =
//                new JdbcUserDetailsManager(dataSource);
//
//
//
//        /*
//            Creates users inside DB tables.
//
//            Spring Security internally uses:
//            - users table
//            - authorities table
//         */
//
//        userDetailsManager.createUser(admin);
//
//        userDetailsManager.createUser(user);
//
//
//
//        return userDetailsManager;
//
//
//
//        /*
//            =====================================================
//                IN-MEMORY USER STORAGE
//            =====================================================
//
//            Stores users only in RAM/memory.
//
//            Used mostly for:
//            - learning
//            - testing
//            - simple apps
//
//            NOT production usually.
//         */
//
////        return new InMemoryUserDetailsManager(admin, user);
//    }
//}
//
//
