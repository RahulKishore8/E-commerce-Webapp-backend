package com.group60.FirstCopyFlipkart.Role;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@Slf4j
public class RoleService {
    private final RoleRepository roleRepository;
    public Role saveRole(Role role){
        return roleRepository.save(role);
    }
    public Role findRoleByName(String name){
        return roleRepository.findRoleByName(name);
    }
}
