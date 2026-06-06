package com.ecommerce.project.security.services;

import com.ecommerce.project.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class UserDetailsImp implements UserDetails {
    private static final long serialVersionUID = 1L;

    //Adding some fields from our side
    private Long id;
    private String username;
    private String email;

    @JsonIgnore
    private String password;
    private Collection<? extends GrantedAuthority> authoritiesList;

    //Developer's Custom method to make user(JPA-Entity) to UserDetails
    public static UserDetailsImp build (User user){

        List<GrantedAuthority>list =
                user.getRoles()
                    .stream()
                        .map(role->new SimpleGrantedAuthority(role.getRoleName().name()))
                        .collect(Collectors.toList());


        return new UserDetailsImp(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getPassword(),
                list
        );
    }


    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsImp that = (UserDetailsImp) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authoritiesList;
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
