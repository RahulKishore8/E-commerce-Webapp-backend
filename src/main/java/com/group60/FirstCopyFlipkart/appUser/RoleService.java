package com.group60.FirstCopyFlipkart.appUser;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@Slf4j
public class RoleService {
    private final RoleRepository roleRepository;
    public Role saveRole(Role role){
        return roleRepository.save(role);
    }
}
