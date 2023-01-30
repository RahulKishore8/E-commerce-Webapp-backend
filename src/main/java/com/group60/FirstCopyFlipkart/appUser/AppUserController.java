package com.group60.FirstCopyFlipkart.appUser;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group60.FirstCopyFlipkart.Role.RoleService;
import com.group60.FirstCopyFlipkart.product.Product;
import com.group60.FirstCopyFlipkart.product.ProductService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
@Slf4j
public class AppUserController {
    private final AppUserService appUserService;
    private final RoleService roleService;
    private final ProductService productService;

    @GetMapping("/get-details")
    public ResponseEntity<AppUser> getAppUserByEmailID(HttpServletRequest request){
        AuthToken authToken = new AuthToken();
        String emailID = authToken.getEmailID(request);
        AppUser appUser = appUserService.findUserByEmailID(emailID);
        if(appUser != null){
            appUser.setPassword("");
            return new ResponseEntity<>(appUser, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.NOT_MODIFIED);
        }
    }

    @GetMapping("/get-cart")
    public ResponseEntity<CartDetailsJSON> getCart(HttpServletRequest request){
        AuthToken authToken = new AuthToken();
        String emailID = authToken.getEmailID(request);
        AppUser appUser = appUserService.findUserByEmailID(emailID);
        if(appUser == null){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ArrayList<CartWithProductElement> list = new ArrayList<>();
        ArrayList<CartItem> userCart = appUser.getCart().getItemList();
        for(CartItem item : userCart){
            Product product = productService.findProductByProductID(item.getProductID());
            int quantity = item.getQuantity();
            CartWithProductElement element = new CartWithProductElement(product, quantity);
            list.add(element);
        }
        int price = appUserService.getCartTotalPrice(emailID);
        CartDetailsJSON details = new CartDetailsJSON(list, price);
        return new ResponseEntity<>(details, HttpStatus.OK);
    }

    @PostMapping("/register")
    public void createNewAppUser(HttpServletResponse response, @RequestBody UserJSON newUserJSON) {
        AppUser user = appUserService.findUserByEmailID(newUserJSON.getEmailID());
        if(user != null){
            response.setHeader("error", "User already exists");
            response.setStatus(HttpStatus.NOT_MODIFIED.value());
        }else{
            AppUser newUser = new AppUser();
            newUser.setUsername(newUserJSON.getUsername());
            newUser.setEmailID(newUserJSON.getEmailID());
            newUser.setPassword(newUserJSON.getPassword());
            newUser.setRole(roleService.findRoleByName("ROLE_CUSTOMER"));
            newUser.setPhoneNumber(newUserJSON.getPhoneNumber());
            newUser.setAddress(new Address());
            newUser.setCart(new Cart());
            newUser.setOrderList(new ArrayList<>());
            newUser.setWalletAmount(1000);
            AppUser createdAppUser = appUserService.insert(newUser);
            if(createdAppUser != null){
                response.setStatus(HttpStatus.OK.value());
            }else{
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        }
    }

    @PatchMapping("/update-phone-number")
    public void updatePhoneNumber(HttpServletRequest request, HttpServletResponse response, @RequestBody UpdatePhoneNumberJSON updatePhoneNumberJSON){
        AuthToken authToken = new AuthToken();
        String emailID = authToken.getEmailID(request);
        AppUser appUser = appUserService.changePhoneNumber(emailID,updatePhoneNumberJSON.getPhoneNumber());
        if(appUser != null){
            response.setStatus(HttpStatus.OK.value());
        }else{
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
    @PatchMapping("/update-emailID")
    public void updateEmailID(HttpServletRequest request, HttpServletResponse response, @RequestBody EmailIDJSON emailIDJSON){
        AuthToken authToken = new AuthToken();
        String emailID = authToken.getEmailID(request);
        AppUser appUser = appUserService.changeEmailID(emailID,emailIDJSON.getEmailID());
        if(appUser != null){
            response.setStatus(HttpStatus.OK.value());
        }else{
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
    @PatchMapping("/update-address")
    public void updateAddress(HttpServletRequest request, HttpServletResponse response, @RequestBody UpdateAddressJSON updateAddressJSON){
        AuthToken authToken = new AuthToken();
        String emailID = authToken.getEmailID(request);
        log.info(updateAddressJSON.getAddress().toString());
        AppUser appUser = appUserService.changeAddress(emailID,updateAddressJSON.getAddress());
        if(appUser != null){
            response.setStatus(HttpStatus.OK.value());
        }else{
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
    @PatchMapping("/update-username")
    public void updateUsername(HttpServletRequest request, HttpServletResponse response, @RequestBody UpdateUsernameJSON updateUsernameJSON){
        AuthToken authToken = new AuthToken();
        String emailID = authToken.getEmailID(request);
        AppUser appUser = appUserService.changeUsername(emailID,updateUsernameJSON.getUsername());
        if(appUser != null){
            response.setStatus(HttpStatus.OK.value());
        }else{
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
    @DeleteMapping("/delete")
    public void deleteAppUser(HttpServletRequest request, HttpServletResponse response){
        AuthToken authToken = new AuthToken();
        String emailID = authToken.getEmailID(request);
        log.info("delete User {}",emailID);
        appUserService.deleteAppUserByEmailID(emailID);
        response.setStatus(HttpStatus.OK.value());
    }

    @PatchMapping("/change-password")
    public void changePassword(HttpServletRequest request, HttpServletResponse response, @RequestBody UpdatePasswordJSON updatePasswordJSON){
        AuthToken authToken = new AuthToken();
        String emailID = authToken.getEmailID(request);
        AppUser appUser = appUserService.changePassword(emailID, updatePasswordJSON.getPassword());
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
                        .withClaim("roles", appUser.getRole().getName())
                        .sign(algorithm);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception exception){
                log.error("error {}", exception.getMessage());
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

    @PatchMapping("/place-order")
    public void placeOrder(HttpServletRequest request, HttpServletResponse response){
        AuthToken authToken = new AuthToken();
        String emailID = authToken.getEmailID(request);
        response.setStatus(appUserService.placeOrder(emailID).value());
        if(response.getStatus() == HttpStatus.NOT_MODIFIED.value()){
            response.setHeader("error","Insufficient Wallet Balance");
        }
    }

    @PatchMapping("/add-balance")
    public void addBalance(HttpServletRequest request, HttpServletResponse response, @RequestBody AddBalanceJSON addBalanceJSON){
        AuthToken authToken = new AuthToken();
        String emailID = authToken.getEmailID(request);
        //log.info("User {}, balance {}",emailID, addBalanceJSON.getBalance());
        AppUser appUser = appUserService.addBalance(emailID, addBalanceJSON.getBalance());
        if(appUser != null){
            response.setStatus(HttpStatus.OK.value());
            //return new ResponseEntity<>(appUser, HttpStatus.OK);
        }else{
            response.setStatus(HttpStatus.NOT_MODIFIED.value());
            //return new ResponseEntity<>(null, HttpStatus.NOT_MODIFIED);
        }
    }

    @PatchMapping("/cart/increment")
    public void incrementItemInCart(HttpServletRequest request, HttpServletResponse response, @RequestBody CartUpdateJSON cartUpdateJSON){
        AuthToken authToken = new AuthToken();
        String emailID = authToken.getEmailID(request);
        AppUser user = appUserService.findUserByEmailID(emailID);
        if(user != null){
            user.incrementProductQuantity(cartUpdateJSON.getProductID());
            AppUser savedUser = appUserService.save(user);
            if(savedUser != null){
                response.setStatus(HttpStatus.OK.value());
            }else{
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        }else{
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @PatchMapping("/cart/decrement")
    public void decrementItemInCart(HttpServletRequest request, HttpServletResponse response, @RequestBody CartUpdateJSON cartUpdateJSON){
        AuthToken authToken = new AuthToken();
        String emailID = authToken.getEmailID(request);
        AppUser user = appUserService.findUserByEmailID(emailID);
        if(user != null){
            user.decrementProductQuantity(cartUpdateJSON.getProductID());
            AppUser savedUser = appUserService.save(user);
            if(savedUser != null){
                response.setStatus(HttpStatus.OK.value());
            }else{
                log.info("error in wallet");
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        }else{
            log.info("error in wallet2");
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<ArrayList<Order>> getAllOrders(HttpServletRequest request, HttpServletResponse response){
        AuthToken authToken = new AuthToken();
        String emailID = authToken.getEmailID(request);
        AppUser user = appUserService.findUserByEmailID(emailID);
        if(user != null){
            return new ResponseEntity<>(user.getOrderList(), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

@Data
class UserJSON {
    private String username;
    private String emailID;
    private String phoneNumber;
    private Address address;
    private String password;
}

@Data
class UpdatePasswordJSON {
    private String password;
}
@Data
class UpdateUsernameJSON {
    private String username;
}
@Data
class EmailIDJSON{
    private String emailID;
}
@Data
class CartUpdateJSON {
    private String productID;
}
@Data
class UpdatePhoneNumberJSON {
    String phoneNumber;
}
@Data
class UpdateAddressJSON {
    Address address;
}
@Data
class AddBalanceJSON {
    int balance;
}

@Data
@AllArgsConstructor
class CartDetailsJSON {
    ArrayList<CartWithProductElement> cart;
    int price;
}

@Data
@AllArgsConstructor
class CartWithProductElement {
    Product product;
    int quantity;
}

class AuthToken{
    public String getEmailID(HttpServletRequest request){
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        String token = authorizationHeader.substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getSubject();
    }
}

