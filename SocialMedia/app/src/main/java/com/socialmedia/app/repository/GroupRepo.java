package com.socialmedia.app.repository;

import com.socialmedia.app.model.SocialGroup;
import com.socialmedia.app.model.SocialUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepo extends JpaRepository<SocialGroup, Long> {
}

