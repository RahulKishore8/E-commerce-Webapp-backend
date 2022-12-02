package com.group60.FirstCopyFlipkart.Admin;

import com.group60.FirstCopyFlipkart.appUser.AppUserService;
import com.group60.FirstCopyFlipkart.product.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
@Slf4j
public class AdminController {
    private final ProductService productService;
    private final AppUserService appUserService;

}
