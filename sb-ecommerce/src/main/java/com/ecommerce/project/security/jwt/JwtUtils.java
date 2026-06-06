package com.ecommerce.project.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${spring.app.jwtExpirationMs}")
    private long jwtExpirationMs;

    @Value("${spring.app.jwtSecretKey}")
    private String jwtSecretKey;

    // Validating  JWT Token: Correct or not
    public boolean validateJwtToken(String authToken){
        //If valid return true otherwise Exception and return false
        //Only Validation No - Reading of Data
        try{
            Jwts
                    .parserBuilder()
                    .setSigningKey(generateKey())
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        }
        catch (MalformedJwtException e){
            logger.error("*Invalid JWT token: {}", e.getMessage());
        }
        catch (ExpiredJwtException e){
            logger.error("*JWT Token is expired: {}", e.getMessage());
        }
        catch (UnsupportedJwtException e){
            logger.error("*JWT Token Unsupported: {}", e.getMessage());
        }
        catch (IllegalArgumentException e){
            logger.error("JWT claims string is Empty: {}", e.getMessage());
        }
        return false;
    }

    //-> Getting JWT Token from Request-Header (for each request coming after login)
    public String getJwtFromHeader(HttpServletRequest request){
        //HttpServletRequest -> Has request Header+Payload
        //JWT Token will come in Header as Authorization: Bearer aacd....

        String bearerToken = request.getHeader("Authorization");
        logger.debug("*Inside Authorization Header: {}", bearerToken);

        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            String JwtToken = bearerToken.substring(7);
            return JwtToken;
        }
        return null;
    }

    //-> Generating JWT Token from userName (In end of Login)
    public String generateJwtTokenFromUserName(UserDetails user){
        /*
                      UserName
                         ↓
                   Use SECRET KEY
                         ↓
                    HMAC SHA256
                         ↓
                  Generate SIGNATURE
                         ↓
                 Attach Signature to JWT
          SIGNATURE = Hash(HEADER + PAYLOAD + SECRET_KEY)
         */

        String userName = user.getUsername();

        // new Date().getTime() -> return time in MS (long) From 1970
        // new Date(long timeMs) will create date object of timeMs

        /* Jwts.builder() : Creates JWT token builder object.
              We now start adding data inside token.   */

        String JwtToken =
                Jwts.builder()
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date (new Date().getTime()+ jwtExpirationMs))
                .signWith(generateKey()) //Attach Key with (Header+Payload)
                .compact(); //   Converts JWT builder data into actual JWT string.

        return JwtToken;
    }

    //-> Generating userName from JWT Token-Used Inside JWTAuthFilter
    public String getUserNameFromJwtToken(String token){
        // -> token example: eyJhbGciOiJIUzI1Ni...
        // -> Jwts.parserBuilder(): Creates JWT parser builder object.

        /* Parser is responsible for:
                -> reading token
                -> validating token
                -> extracting data from token
         */

        //.build():  Builds final JWT parser object.

        /*
              ## Most Important - Checking & Validation of Token ##
              .parseClaimsJws(token): actual Parsing JWT token.

              To - verifies:
                 -> signature
                 -> token structure
                 -> expiry

            $ If token invalid: exception occurs $
         */

        /*
            ->.setSigningKey(Key) : used to validate & checking Token
            ->.signWith(Key): used to CREATE a Token

         */
                  //Validation -> then -> Reading
        String userName =
                Jwts.parserBuilder()
                        .setSigningKey(generateKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody().getSubject();
        return userName;
    }

    public Key generateKey(){
            /*
                Take Base64 secret string
                        ↓
                Decode into bytes
                        ↓
                Create cryptographic signing key
                 Algorithm: HMAC SHA256
                        ↓
                Return Key object
            */
            return Keys.hmacShaKeyFor(
                    Decoders.BASE64.decode(jwtSecretKey)
            );
    }

}
/*
    Implementing Login/Signup - Helping methods
-> Getting JWT Token from Request-Header (for each request after login)
-> Generating JWT Token from userName (In end of Login)
-> Generating userName from JWT Token (Used inside JwtAuthFilter)
-> Generating Signing Key: used in getUserNameFromJwtToken/GenerateJwtToken/ValidateToken all....
-> Validate JWT Token (Checking TOKEN is correct/UnExpired/Valid etc..)
*/
