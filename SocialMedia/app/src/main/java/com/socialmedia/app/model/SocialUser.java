package com.socialmedia.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocialUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String userName;

    @OneToOne(mappedBy="socialUser" , cascade = CascadeType.ALL)
    private SocialProfile userSocialProfile;

    @OneToMany(mappedBy = "socialUser")
    private List<SocialPost> userSocialPosts;

    //Below Relation will create new Table.
    //So, @JoinColumn make no sense.
    //Therefore, @JoinTable will be used.
    @ManyToMany
    @JoinTable(
            //New Table Name
            name = "user_group_table",
            //Name of Column representing this(SocialUser) class/Entity's key
            joinColumns = @JoinColumn(name="user_id"),
            //Name of Column representing other(SocialGroup) class/Entity's key & having  "mappedBy".
            inverseJoinColumns = @JoinColumn(name="group_id")
    )
    private Set<SocialGroup> userSocialGroups = new HashSet<>();

    @Override
    public int hashCode(){
        return Objects.hash(this.userId);
    }


    public void setUserSocialProfile(SocialProfile socialProfile){
        socialProfile.setSocialUser(this);
        this.userSocialProfile = socialProfile;
    }
}
