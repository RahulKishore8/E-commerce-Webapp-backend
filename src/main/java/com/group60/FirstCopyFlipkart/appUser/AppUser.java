package com.group60.FirstCopyFlipkart.appUser;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class AppUser {
    @Id
    private String userID;
    private String username;
    private String emailID;
    private Role role;
    private String phoneNumber;
    private Address address;
    private String password;
}
