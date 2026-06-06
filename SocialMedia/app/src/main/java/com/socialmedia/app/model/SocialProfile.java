package com.socialmedia.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocialProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    private String profileDescription;

    @OneToOne
    @JoinColumn(name = "social_user_id")
    @JsonIgnore
    private SocialUser socialUser;

    @Override
    public int hashCode(){
        return Objects.hash(this.profileId);
    }

    public void setSocialUser(SocialUser socialUser){
        socialUser.setUserSocialProfile(this);
        this.socialUser = socialUser;
    }
}
