package com.ecommerce.project.security.services;

import com.ecommerce.project.model.User;
import com.ecommerce.project.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
//Why Bean?
//Because It's bean needed in JwtAuthFilter (Custom filter)
//Because Login API need it's method to extract user from DB

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found Exception"));

        UserDetails userDetailsObj = UserDetailsImp.build(userEntity);
        return userDetailsObj;
    }
}
