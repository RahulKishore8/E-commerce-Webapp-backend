package com.group60.FirstCopyFlipkart.appUser;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppUserRepository extends MongoRepository<AppUser, String> {
    //Access
    AppUser findAppUserByEmailID(String emailID);

    //Create
    AppUser insert(AppUser appUser);

    //Update
    AppUser save(AppUser appUser);

    //Delete
    void deleteAppUserByEmailID(String emailID);

}
