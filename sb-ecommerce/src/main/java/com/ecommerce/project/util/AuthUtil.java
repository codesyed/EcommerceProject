package com.ecommerce.project.util;

import com.ecommerce.project.model.User;
import com.ecommerce.project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    @Autowired
    UserRepository userRepository;

    public String loggedInEmail(){
        Authentication authenticationObject =
                SecurityContextHolder.getContext().getAuthentication();

        //Respective User exists in Database or Not
        User user = userRepository.findByUserName(authenticationObject.getName())
                .orElseThrow(()-> new UsernameNotFoundException("User Not Found In-Database"));

         return user.getEmail();
    }

    public Long loggedInUserId(){
        Authentication authenticationObject =
                SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByUserName(authenticationObject.getName())
                .orElseThrow(()->new UsernameNotFoundException("User Not Found In-Database"));

        return user.getUserId();
    }

    public User loggedInUser(){
        Authentication authenticationObject =
                SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByUserName(authenticationObject.getName())
                .orElseThrow(()->new UsernameNotFoundException("User Not Found In-Database"));
        return user;
    }
}
