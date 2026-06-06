package com.socialmedia.app.repository;

import com.socialmedia.app.model.SocialPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepo extends JpaRepository<SocialPost, Long> {
}

