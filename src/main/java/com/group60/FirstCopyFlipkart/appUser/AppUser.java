package com.group60.FirstCopyFlipkart.appUser;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;

@Data
public class AppUser {
    @Id
    private String userID;
    private String username;
    private String emailID;
    private String role;
    private String phoneNumber;
    private Address address;
    private String password;
    private int walletAmount;
    private Cart cart;
    private ArrayList<Order> orderList;

    public void incrementProductQuantity(String productID){
        cart.incrementProductQuantity(productID);
    }

    public void decrementProductQuantity(String productID){
        cart.decrementProductQuantity(productID);
    }
}
