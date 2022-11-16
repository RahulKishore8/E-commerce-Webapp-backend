package com.group60.FirstCopyFlipkart.appUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class AppUserController {
    private final AppUserService appUserService;

    @GetMapping
    public ResponseEntity<AppUser> getAppUserByEmailID(@RequestBody EmailID emailID){
        AppUser appUser = appUserService.findUserByEmailID(emailID.emailID);
        if(appUser != null){
            appUser.setPassword("");
            return new ResponseEntity<>(appUser, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<AppUser> createNewAppUser(@RequestBody AppUser appUser){
        AppUser createdAppUser = appUserService.insert(appUser);
        if(appUser != null){
            appUser.setPassword("");
            return new ResponseEntity<>(appUser, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<AppUser> deleteAppUser(@RequestBody AppUser appUser){
        appUserService.deleteAppUser(appUser);
        return new ResponseEntity<AppUser>(appUser, HttpStatus.OK);
    }
}

@Data
class EmailID{
    String emailID;
}