package com.group60.FirstCopyFlipkart.appUser;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

@Data
@AllArgsConstructor
public class Order {
    private String id;
    private ArrayList<CartItem> itemList;
    private Date orderDate;
    private String status;
    private int orderTotal;
}
