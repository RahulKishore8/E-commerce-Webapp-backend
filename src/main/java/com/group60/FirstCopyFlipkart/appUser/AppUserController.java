package com.group60.FirstCopyFlipkart.appUser;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@Slf4j
public class AppUserController {
    private final AppUserService appUserService;

    @GetMapping
    public ResponseEntity<AppUser> getAppUserByEmailID(@RequestBody EmailID emailID){
        AppUser appUser = appUserService.findUserByEmailID(emailID.emailID);
        if(appUser != null){
            appUser.setPassword("");
            return new ResponseEntity<>(appUser, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<AppUser> createNewAppUser(@RequestBody AppUser appUser){
        AppUser createdAppUser = appUserService.insert(appUser);
        if(appUser != null){
            appUser.setPassword("");
            return new ResponseEntity<>(appUser, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try{
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String emailID = decodedJWT.getSubject();
                AppUser appUser = appUserService.findUserByEmailID(emailID);
                String access_token = JWT.create()
                        .withSubject(appUser.getEmailID())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10*60*1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", appUser.getRole())
                        .sign(algorithm);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception exception){
                log.error("error", exception.getMessage());
                response.setHeader("error", exception.getMessage());
                //response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else{
            log.error("refresh token is missing");
        }
    }
    @DeleteMapping
    public ResponseEntity<AppUser> deleteAppUser(@RequestBody AppUser appUser){
        appUserService.deleteAppUser(appUser);
        return new ResponseEntity<AppUser>(appUser, HttpStatus.OK);
    }



}

@Data
class EmailID{
    String emailID;
}