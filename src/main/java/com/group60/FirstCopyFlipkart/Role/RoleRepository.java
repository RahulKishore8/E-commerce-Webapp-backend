package com.group60.FirstCopyFlipkart.Role;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "localhost:3000")
public interface RoleRepository extends MongoRepository<Role, String> {
    Role findRoleByName(String name);
    Role save(Role role);
    Role deleteRoleByName(String name);
}
