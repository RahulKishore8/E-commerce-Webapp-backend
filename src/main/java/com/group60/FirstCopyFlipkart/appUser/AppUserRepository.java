package com.group60.FirstCopyFlipkart.appUser;

import com.group60.FirstCopyFlipkart.Role.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AppUserRepository extends MongoRepository<AppUser, String> {
    //Access
    AppUser findAppUserByEmailID(String emailID);

    //Create
    AppUser insert(AppUser appUser);

    //Update
    AppUser save(AppUser appUser);

    List<AppUser> findAllByRole(Role role);
    //Delete
    void deleteAppUserByEmailID(String emailID);

}
