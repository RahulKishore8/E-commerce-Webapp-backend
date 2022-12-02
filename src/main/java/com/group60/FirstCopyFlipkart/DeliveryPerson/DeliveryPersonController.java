package com.group60.FirstCopyFlipkart.DeliveryPerson;

import com.group60.FirstCopyFlipkart.appUser.AppUserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/delivery-person")
@AllArgsConstructor
public class DeliveryPersonController {
    private final AppUserService appUserService;
    @GetMapping("/update-delivery-status")
    void updateDeliveryStatus(HttpServletResponse response, @RequestBody UpdateDeliveryJSON updateDeliveryJSON){
        response.setStatus(appUserService.updateOrderStatus(updateDeliveryJSON.getEmailID(), updateDeliveryJSON.getOrderID(), updateDeliveryJSON.getStatus()).value());
    }
}

@Data
class UpdateDeliveryJSON {
    private String emailID;
    private String orderID;
    private String status;
}