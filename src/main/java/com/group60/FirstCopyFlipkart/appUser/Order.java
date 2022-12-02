package com.group60.FirstCopyFlipkart.appUser;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Order {
    private String id;
    private Cart cart;
    private Date orderDate;
    private String status;
}
