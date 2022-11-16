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

    public Product(String productName, String categoryName, float price,
                   float discount, int deliveryTime, int quantity) {
        this.productName = productName;
        this.categoryName = categoryName;
        this.price = price;
        this.discount = discount;
        this.deliveryTime = deliveryTime;
        this.quantity = quantity;
    }
}
