package com.group60.FirstCopyFlipkart.appUser;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@AllArgsConstructor
@Service
@Slf4j
public class AppUserService implements UserDetailsService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String emailID) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findAppUserByEmailID(emailID);
        if(appUser == null) {
            log.error("User not found");
            throw new UsernameNotFoundException("User not found in database");
        }else{
            log.info("User found in database: {}", emailID);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(appUser.getRole()));
        return new User(appUser.getUsername(), appUser.getPassword(), authorities);
    }
    //Access
    AppUser findUserByEmailID(String emailID){
        return appUserRepository.findAppUserByEmailID(emailID);
    }

    //Insert
    AppUser insert(AppUser appUser){
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        return appUserRepository.insert(appUser);
    }
    AppUser save(AppUser appUser){
        return appUserRepository.save(appUser);
    }

    //Change Password
    AppUser changePassword(String emailID, String newPassword){
        AppUser appUser = findUserByEmailID(emailID);
        if(appUser != null){
            appUser.setPassword(passwordEncoder.encode(newPassword));
            return appUserRepository.save(appUser);
        } else{
            return null;
        }

    }

    //Change Address
    AppUser changeAddress(AppUser appUser, Address newAddress){
        appUser.setAddress(newAddress);
        return appUserRepository.save(appUser);
    }

    //Change PhoneNumber
    AppUser changePhoneNumber(AppUser appUser, String newPhoneNumber){
        appUser.setPhoneNumber(newPhoneNumber);
        return appUserRepository.save(appUser);
    }
    //Delete User
    void deleteAppUserByEmailID(String emailID){
        appUserRepository.deleteAppUserByEmailID(emailID);
    }


}
