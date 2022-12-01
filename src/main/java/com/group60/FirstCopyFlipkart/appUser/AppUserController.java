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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
@Slf4j
public class AppUserController {
    private final AppUserService appUserService;

    @GetMapping
    public ResponseEntity<AppUser> getAppUserByEmailID(HttpServletRequest request, @RequestBody EmailIDJSON emailID){
        AppUser appUser = appUserService.findUserByEmailID(emailID.emailID);
        if(appUser != null){
            appUser.setPassword("");
            return new ResponseEntity<>(appUser, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.NOT_MODIFIED);
        }
    }

    @PostMapping
    public ResponseEntity<AppUser> createNewAppUser(@RequestBody UserJSON newUserJSON){
        AppUser newUser = new AppUser();
        newUser.setUsername(newUserJSON.getUsername());
        newUser.setEmailID(newUserJSON.getEmailID());
        newUser.setPassword(newUserJSON.getPassword());
        newUser.setRole(newUserJSON.getRole());
        newUser.setPhoneNumber(newUserJSON.getPhoneNumber());
        newUser.setAddress(newUserJSON.getAddress());
        newUser.setWalletAmount(1000);

        AppUser createdAppUser = appUserService.insert(newUser);
        if(createdAppUser != null){
            createdAppUser.setPassword("");
            return new ResponseEntity<>(createdAppUser, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.NOT_MODIFIED);
        }
    }

    @PatchMapping
    public ResponseEntity<AppUser> updateAppUser(@RequestBody UserJSON updatedUserJSON){
        AppUser user = appUserService.findUserByEmailID(updatedUserJSON.getEmailID());
        if(user != null){
            user.setUsername(updatedUserJSON.getUsername());
            user.setEmailID(updatedUserJSON.getEmailID());
            user.setRole(updatedUserJSON.getRole());
            user.setPhoneNumber(updatedUserJSON.getPhoneNumber());
            user.setAddress(updatedUserJSON.getAddress());
            user = appUserService.save(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.NOT_MODIFIED);
        }
    }

    @DeleteMapping
    public void deleteAppUser(HttpServletRequest request, HttpServletResponse response, @RequestBody EmailIDJSON emailID){
        appUserService.deleteAppUserByEmailID(emailID.getEmailID());
        response.setStatus(HttpStatus.OK.value());
    }

    @PatchMapping("/change-password")
    public void changePassword(HttpServletRequest request, HttpServletResponse response, @RequestBody UpdatePasswordJSON updatePasswordJSON){
        AppUser appUser = appUserService.changePassword(updatePasswordJSON.getEmailID(), updatePasswordJSON.getPassword());
        if(appUser != null){
            response.setStatus(HttpStatus.OK.value());
        }else{
            response.setStatus(HttpStatus.NOT_MODIFIED.value());
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

    @GetMapping("/place-order")
    public void placeOrder(HttpServletRequest request, HttpServletResponse response, @RequestBody EmailIDJSON emailIDJSON){
        AppUser user = appUserService.findUserByEmailID(emailIDJSON.getEmailID());
        int totalPrice = user.getCart().getTotalPrice();
        if(user.getWalletAmount() >= totalPrice){
            user.setWalletAmount(user.getWalletAmount() - totalPrice);
            user.getOrderList().add(new Order(user.getCart(),new Date(), "order placed"));
            user.getCart().setItemList(new ArrayList<>());
            response.setStatus(HttpStatus.OK.value());
        }else{
            response.setStatus(HttpStatus.NOT_MODIFIED.value());
        }
    }

    @GetMapping("/cart/increment")
    public void incrementItemInCart(HttpServletRequest request, HttpServletResponse response, @RequestBody EmailIDJSON emailIDJSON){
        AppUser user = appUserService.findUserByEmailID(emailIDJSON.getEmailID());
        int totalPrice = user.getCart().getTotalPrice();
        if(user.getWalletAmount() >= totalPrice){
            user.setWalletAmount(user.getWalletAmount() - totalPrice);
            user.getOrderList().add(new Order(user.getCart(),new Date(), "order placed"));
            user.getCart().setItemList(new ArrayList<>());
            response.setStatus(HttpStatus.OK.value());
        }else{
            response.setStatus(HttpStatus.NOT_MODIFIED.value());
        }
    }

}

@Data
class EmailIDJSON{
    String emailID;
}
@Data
class UserJSON {
    private String username;
    private String emailID;
    private String role;
    private String phoneNumber;
    private Address address;
    private String password;
}

@Data
class UpdatePasswordJSON {
    private String emailID;
    private String password;
}

@Data
class CartUpdateJSON {
    private String emailID;
    private String productID;
}