package com.group60.FirstCopyFlipkart.appUser;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AppUserService {
    private final AppUserRepository appUserRepository;

    //Access
    AppUser findUserByEmailID(String emailID){
        return appUserRepository.findAppUserByEmailID(emailID);
    }

    //Insert
    AppUser insert(AppUser appUser){
        return appUserRepository.insert(appUser);
    }

    //Change Password
    AppUser changePassword(AppUser appUser, String newPassword){
        appUser.setPassword(newPassword);
        return appUserRepository.save(appUser);
    }

    //Change Address
    AppUser changePassword(AppUser appUser, Address newAddress){
        appUser.setAddress(newAddress);
        return appUserRepository.save(appUser);
    }

    //Change PhoneNumber
    AppUser changePhoneNumber(AppUser appUser, String newPhoneNumber){
        appUser.setPhoneNumber(newPhoneNumber);
        return appUserRepository.save(appUser);
    }
    //Delete User
    void deleteAppUser(AppUser appUser){
        appUserRepository.deleteAppUserByEmailID(appUser.getEmailID());
    }
}
