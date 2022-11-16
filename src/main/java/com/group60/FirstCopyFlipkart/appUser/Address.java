package com.group60.FirstCopyFlipkart.appUser;

import lombok.Data;

@Data
public class Address {
    private String line1;
    private String line2;
    private String city;
    private String district;
    private String state;
    private int pinCode;
}
