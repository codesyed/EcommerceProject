//package com.example.securitydemo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.JdbcUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//import javax.sql.DataSource;
//
//import static org.springframework.security.config.Customizer.withDefaults;
//
//@Configuration //To enable @Bean-Creation
//@EnableWebSecurity //To Specify that this class will have 'Custom Spring Security Configuration'
//@EnableMethodSecurity //To Enable WORKING OF -> @PreAuthorize() & &PostAuthorize() used inside Controller.
//public class SecurityConfig {
//
//    @Autowired
//    //Spring boot is smart enough to check pom.xml and app.properties
//    //& creates a datasource bean automatically, we just need to inject.
//    DataSource dataSource;
//
//    @Bean
//    SecurityFilterChain mySecurityConfig(HttpSecurity http) throws Exception {
//
//        //Making all endPoints secure
//        http.authorizeHttpRequests(requests ->
//                requests.requestMatchers("/h2-console/**").permitAll()
//                        .anyRequest().authenticated());
//        //Making sessionManagement to Stateless
//        http.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//        http.httpBasic(withDefaults());
////        http.formLogin(withDefaults());
//
//        http.headers(headers->headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
//        http.csrf(AbstractHttpConfigurer::disable);
//        return http.build();
//    }
//    /*
//        UserDetails → actual user information
//        UserDetailsService → fetch/load user information
//        InMemoryUserDetailsManager → loads/manages users from memory
//    */
//    //Creation of InMemoryUsers
//    @Bean
//    public UserDetailsService userDetailsService(){
//        UserDetails user = User.withUsername("syed")
//                .password("12345") //{noop} to avoid passwd encoding/hashing
//                .roles("USER")
//                .build();
//
//        UserDetails admin = User.withUsername("admin")
//                .password(makePasswordEncoder().encode("admin"))
//                .roles("ADMIN")
//                .build();
//        //JdbcUserDetailsManager is also implementation of UserDetailsService
//        //Used when to create users from code To Database
//        JdbcUserDetailsManager userDetailsManager=new JdbcUserDetailsManager(dataSource);
//        userDetailsManager.createUser(admin);
//        userDetailsManager.createUser(user);
//        return userDetailsManager;
//
//        //InMemoryUserDetailsManager is implementation of UserDetailsService
//        //InMemoryUserDetailsManager accepts object or array of "UserDetails"
////        return new InMemoryUserDetailsManager(admin, user);
//    }
//
//    @Bean
//    public PasswordEncoder makePasswordEncoder(){
//        //BCryptPasswordEncoder is one of the implementation of PasswordEncoder
//        //Mostly Preferred implementation of PasswordEncoder is BCryptPasswordEncoder
//        return new BCryptPasswordEncoder();
//    }
//}