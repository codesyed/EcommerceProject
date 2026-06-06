package com.socialmedia.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocialGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    private String groupHeading;

    @ManyToMany(mappedBy = "userSocialGroups")
    @JsonIgnore
    private Set<SocialUser> users = new HashSet<>();

    @Override
    public int hashCode(){
        return Objects.hash(this.groupId);
    }
}
