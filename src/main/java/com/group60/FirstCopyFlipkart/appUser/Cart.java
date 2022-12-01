package com.group60.FirstCopyFlipkart.appUser;

import com.group60.FirstCopyFlipkart.product.Product;
import com.group60.FirstCopyFlipkart.product.ProductRepository;
import com.group60.FirstCopyFlipkart.product.ProductService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

@Data @AllArgsConstructor
public class Cart {
    ArrayList<CartItem> itemList;
    private final ProductService productService;
    int getTotalPrice(){
        int totalPrice = 0;
        for(int i = 0; i < itemList.size(); i++){
            String productID = itemList.get(i).getProductID();
            Product product = productService.findProductByProductID(productID);
            float productPrice = product.getPrice();
            totalPrice += itemList.get(i).getQuantity() * productPrice;
        }
        return totalPrice;

    }

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




