package com.socialmedia.app.repository;

import com.socialmedia.app.model.SocialPost;
import com.socialmedia.app.model.SocialProfile;
import com.socialmedia.app.model.SocialUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepo extends JpaRepository<SocialProfile, Long> {
}

