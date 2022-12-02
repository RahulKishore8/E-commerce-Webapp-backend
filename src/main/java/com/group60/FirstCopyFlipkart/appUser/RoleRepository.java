package com.group60.FirstCopyFlipkart.appUser;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {
    Role findRoleByName(String name);
    Role save(Role role);
    Role deleteRoleByName(String name);
}
