package com.socialmedia.app.service;

import com.socialmedia.app.model.SocialUser;
import com.socialmedia.app.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SocialService {

    @Autowired
    UserRepo userRepo;

    public List<SocialUser> getAllUsers() {
        return userRepo.findAll();
    }

    public SocialUser saveUser(SocialUser user){
        return userRepo.save(user);
    }
}
