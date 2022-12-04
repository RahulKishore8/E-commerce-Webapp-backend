package com.group60.FirstCopyFlipkart.appUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data

public class Cart {
    ArrayList<CartItem> itemList;

    public Cart() {
        itemList = new ArrayList<>();
    }

    void incrementProductQuantity(String productID){
        boolean presentInCart = false;
        for (CartItem cartItem : itemList) {
            if (cartItem.getProductID().equals(productID)) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                presentInCart = true;
                break;
            }
        }
        if(!presentInCart){
            CartItem newCartItem = new CartItem(productID,1);
            itemList.add(newCartItem);
        }
    }
    void decrementProductQuantity(String productID){
        for (CartItem cartItem : itemList) {
            if (cartItem.getProductID().equals(productID)) {
                if(cartItem.getQuantity() == 1){
                    itemList.remove(cartItem);
                }else{
                    cartItem.setQuantity(cartItem.getQuantity() - 1);
                }
                break;
            }
        }
    }
}




