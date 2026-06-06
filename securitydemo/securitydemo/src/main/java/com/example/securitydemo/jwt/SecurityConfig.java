package com.example.securitydemo.jwt;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@Configuration //To enable @Bean-Creation
@EnableWebSecurity //To Specify that this class will have 'Custom Spring Security Configuration'
@EnableMethodSecurity //To Enable WORKING OF -> @PreAuthorize() & @PostAuthorize() used inside Controller.
public class SecurityConfig {

    @PostConstruct
    public void init(){
        System.out.println("** Check:-> SecurityConfig Execution Completed!");
    }

//    What is DataSource in Spring Boot? 😭🔥
//    DataSource represents the database connection object/provider in Java.
//    -> It is responsible for giving database connections whenever the application needs
//    -> to communicate with the DB.

    /*
        application.properties (spring.datasource.url | spring.datasource.username | spring.datasource.password)
                ↓
        Spring Boot AutoConfiguration
                ↓
        Automatic-DataSource Implementation and Bean created (HikariDataSource)
                 ↓
        Injected wherever DataSource required

        DataSource does NOT store data Rather manages DB connections.
     */
    @Autowired
    private DataSource dataSource;

    @Autowired
    private AuthEntryPoint customUnAuthorizedHandler; //To Manual Handle 401

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler; //To Manual Handle 403

    @Bean //This bean will be used in Authenticating username +passwd in 'LOGIN' .authenticate()...
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception{
        return builder.getAuthenticationManager();
    }

    @Bean //To Apply filter created By Us
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public SecurityFilterChain mySecurityConfig(HttpSecurity http) throws Exception {

        //Making EndPoints secure
        //.permitAll(): These endPoint doesn't require anyAuthentication when reaches here let all access it.
        //.permitAll(): Doesn't remove SecurityFilters execution just let these request to pass on...

        // No *  -> Exact match only | * -> Matches ONE path level |  **    -> Matches ANY number of path levels
        http.authorizeHttpRequests(
                requests ->
                requests.requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/signin/**").permitAll()
                        .anyRequest().authenticated());

        //Making sessionManagement to Stateless
        http.sessionManagement(
                session->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // http.httpBasic(withDefaults()); --> No need with JWT
        // http.formLogin(withDefaults()); --> No need with JWT

        // Custom Exception Handling 401 and 403
        http.exceptionHandling(
                exception -> exception
                        .authenticationEntryPoint(customUnAuthorizedHandler) //401
                        .accessDeniedHandler(customAccessDeniedHandler) //403
        );


        /*
            formLogin() activates Spring's default login mechanism using "UsernamePasswordAuthenticationFilter"

            In custom JWT authentication,we create our own login API, so this filter no longer handles authentication logic.

            It still exists internally in filter chain,
            but mainly acts as a filter-order reference point and does not interfere with custom JWT login flow.
        */

        http.addFilterBefore(
                authenticationJwtTokenFilter(),
                UsernamePasswordAuthenticationFilter.class
        );

        http.headers(headers->headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    /*
        UserDetails → actual user information
        UserDetailsService → fetch/load user information
        InMemoryUserDetailsManager(implementation of UserDetailsService) → loads/manages users from memory
        JdbcUserDetailsManager(implementation of UserDetailsService) → loads/manages users from Database.
    */

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        //JdbcUserDetailsManager is also implementation of UserDetailsService
        //Used when to create users from code To Database
        return new JdbcUserDetailsManager(dataSource);

        // InMemoryUserDetailsManager is implementation of UserDetailsService
        // InMemoryUserDetailsManager accepts object or array of "UserDetails"
        // return new InMemoryUserDetailsManager(admin, user);
    }

    @Bean
    public CommandLineRunner initData(UserDetailsService userDetailsService) {
        return args -> {

            //UserDetailsService is Parent can be type cast to Child
            JdbcUserDetailsManager manager =
                    (JdbcUserDetailsManager) userDetailsService;

            UserDetails user1 = User.withUsername("syed")
                    .password("{noop}12345") //{noop} to avoid passwd encoding/hashing
                    .roles("USER")
                    .build();

            UserDetails admin = User.withUsername("admin")
                    .password(makePasswordEncoder().encode("admin"))
                    .roles("ADMIN")
                    .build();

            manager.createUser(user1);
            manager.createUser(admin);
        };
    }

    @Bean
    public PasswordEncoder makePasswordEncoder(){
        //BCryptPasswordEncoder is one of the implementation of PasswordEncoder
        //Mostly Preferred implementation of PasswordEncoder is BCryptPasswordEncoder
        return new BCryptPasswordEncoder();
    }
}