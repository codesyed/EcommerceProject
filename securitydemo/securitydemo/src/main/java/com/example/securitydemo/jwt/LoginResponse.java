package com.example.securitydemo.jwt;

import java.util.List;

public class LoginResponse {
  private String JwtToken;
  private String userName;
  private List<String> roles;

  public LoginResponse(String jwtToken, String userName, List<String> roles) {
        JwtToken = jwtToken;
        this.userName = userName;
        this.roles = roles;
  }

    public String getJwtToken() {
        return JwtToken;
    }

    public void setJwtToken(String jwtToken) {
        JwtToken = jwtToken;
    }

    public String getUserNme() {
        return userName;
    }

    public void setUserNme(String userNme) {
        this.userName = userName;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
