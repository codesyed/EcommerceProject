package com.example.securitydemo.jwt;

import tools.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component //AccessDeniedHandler
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        //Step01: Set response type to Json: Response body will be JSON
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        //Step02: Set status code for Response: Set HTTP Status Code = 401
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); //403

        //Step03: What to return as Customized response
        Map<String, Object> map = new HashMap<>();

        map.put("-> status", 403);
        map.put("-> error", "Forbidden");
        map.put("-> message", accessDeniedException.getMessage());
        map.put("-> path", request.getServletPath());

        //Step04: Putting this map to Response body
        //ObjectMapper used to Convert Java Object TO JSON & Other format & ViceVersa
        //Here we are doing manually, but in ResponseEntity SpingBoot does it on our behalf
        //import tools.'jackson'.databind.ObjectMapper;
        ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(response.getOutputStream(), map);
    }
}