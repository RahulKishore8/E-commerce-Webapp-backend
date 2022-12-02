package com.group60.FirstCopyFlipkart.appUser;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data @AllArgsConstructor
public class Cart {
    ArrayList<CartItem> itemList;

    void incrementProductQuantity(String productID){
        CartItem newCartItem = new CartItem(productID,1);
        if(itemList.contains(newCartItem)){
            int index = itemList.indexOf(newCartItem);
            newCartItem.setQuantity(itemList.get(index).getQuantity()+1);
            itemList.set(index,newCartItem);
        }else{
            itemList.add(newCartItem);
        }
    }
    void decrementProductQuantity(String productID){
        CartItem newCartItem = new CartItem(productID,1);
        if(itemList.contains(newCartItem)){
            int index = itemList.indexOf(newCartItem);
            if(itemList.get(index).getQuantity() > 1){
                newCartItem.setQuantity(itemList.get(index).getQuantity()-1);
                itemList.set(index,newCartItem);
            }else{
                itemList.remove(index);
            }
        }
    }
}




