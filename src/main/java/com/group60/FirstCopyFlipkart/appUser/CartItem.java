package com.group60.FirstCopyFlipkart.appUser;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItem {
    String productID;
    int quantity;

    public boolean equals(CartItem cartItem){
        return(cartItem.productID.equals(this.productID));
    }
}
