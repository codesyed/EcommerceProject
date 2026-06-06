package com.ecommerce.project.security.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//->This class(AuthenticationEntryPoint) handles authentication failure response: 401
//-> Custom response sender for unauthorized requests (401 errors)
@Component//AuthenticationEntryPoint
public class AuthEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException e) throws IOException, ServletException {
        logger.error("Unauthorized error: {}", e.getMessage());

        //Step01: Set response type to Json: Response body will be JSON
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        //Step02: Set status code for Response: Set HTTP Status Code = 401
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //401

        //Step03: What to return as Customized response
        final Map<String, Object > map = new HashMap<>();
        map.put("-> status", HttpServletResponse.SC_UNAUTHORIZED);
        map.put("-> error", "Un-Authorized");
        map.put("-> Error Generated Msg: ", e.getMessage());
        map.put("-> API-PATH", request.getServletPath());

        //Step04: Putting this map to Response body
        //ObjectMapper used to Convert Java Object TO JSON & Other format & ViceVersa
        //Here we are doing manually, but in ResponseEntity SpingBoot does it on our behalf
        //import tools.'jackson'.databind.ObjectMapper;

        final ObjectMapper mapper=new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), map);
    }
}
