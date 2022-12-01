package com.group60.FirstCopyFlipkart.product;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Product {
    @Id
    private String productID;
    private String productName;
    private String categoryName;
    private float price;
    private float discount;
    private int deliveryTime;
    private int quantity;
    private String imageURL;
    private int orderCount;
    public Product(String productName, String categoryName, float price,
                   float discount, int deliveryTime, int quantity, String imageURL) {
        this.productName = productName;
        this.categoryName = categoryName;
        this.price = price;
        this.discount = discount;
        this.deliveryTime = deliveryTime;
        this.quantity = quantity;
        this.imageURL = imageURL;
        this.orderCount = 0;
    }

    public void incrementOrderCount(int count){
        orderCount = orderCount + count;
    }
}
