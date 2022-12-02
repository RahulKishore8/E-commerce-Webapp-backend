package com.group60.FirstCopyFlipkart.appUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/role")
@AllArgsConstructor
@Slf4j
public class RoleController {
    private final RoleService roleService;
    @PostMapping
    public ResponseEntity<Role> createNewAppUser(@RequestBody Role newRole){
        Role role = roleService.saveRole(newRole);
        if(role != null){
            return new ResponseEntity<>(role, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.NOT_MODIFIED);
        }
    }
}


