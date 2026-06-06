package com.ecommerce.project.security.response;

import java.util.List;

public class LoginResponse {
    private Long id;
    private String JwtToken;
    private String userName;
    private List<String> roles;

    public LoginResponse(Long id, String jwtToken, String userName, List<String> roles) {
        this.id = id;
        this.JwtToken = jwtToken;
        this.userName = userName;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getJwtToken() {
        return JwtToken;
    }

    public void setJwtToken(String jwtToken) {
        JwtToken = jwtToken;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
