package com.group60.FirstCopyFlipkart.Admin;

import com.group60.FirstCopyFlipkart.Role.RoleService;
import com.group60.FirstCopyFlipkart.appUser.Address;
import com.group60.FirstCopyFlipkart.appUser.AppUser;
import com.group60.FirstCopyFlipkart.appUser.AppUserService;
import com.group60.FirstCopyFlipkart.product.Product;
import com.group60.FirstCopyFlipkart.product.ProductService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
@Slf4j
public class AdminController {
    private final ProductService productService;
    private final AppUserService appUserService;
    private final RoleService roleService;

    @GetMapping("/report")
    public ResponseEntity<ArrayList<ReportJSON>> getReport(){
        List<Product> productList = productService.getAllProducts();
        ArrayList<ReportJSON> responseList = new ArrayList<>();
        for(Product product: productList){
            ReportJSON reportJSON = new ReportJSON(product.getProductName(), product.getOrderCount());
            responseList.add(reportJSON);
        }
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }
    @GetMapping("/item-sales")
    public ResponseEntity<ArrayList<ProductSalesOnDate>> getOrdersOnDate(@RequestBody DateJSON dateJSON){
        HashMap<String, Integer> ordersOnDate = appUserService.getOrdersOnDate(dateJSON.getDate());
        if(ordersOnDate == null){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ArrayList<ProductSalesOnDate> ordersOnDateJSON = new ArrayList<>();
        for(Map.Entry<String,Integer> mapElement: ordersOnDate.entrySet()){
            String productID = mapElement.getKey();
            Product product = productService.findProductByProductID(productID);
            ProductSalesOnDate entry = new ProductSalesOnDate(product, mapElement.getValue());
            ordersOnDateJSON.add(entry);
        }
        return new ResponseEntity<>(ordersOnDateJSON, HttpStatus.OK);
    }
    @PostMapping("/register-manager")
    public void createManager(HttpServletResponse response, @RequestBody NewManagerJSON newManagerJSON) {
        AppUser user = appUserService.findUserByEmailID(newManagerJSON.getEmailID());
        if(user != null){
            response.setHeader("error", "User already exists");
            response.setStatus(HttpStatus.NOT_MODIFIED.value());
        }else{
            AppUser newUser = new AppUser();
            newUser.setUsername(newManagerJSON.getUsername());
            newUser.setEmailID(newManagerJSON.getEmailID());
            newUser.setPassword(newManagerJSON.getPassword());
            newUser.setRole(roleService.findRoleByName("ROLE_MANAGER"));
            newUser.setPhoneNumber(newManagerJSON.getPhoneNumber());
            newUser.setAddress(new Address());
            newUser.setWalletAmount(0);
            AppUser createdAppUser = appUserService.insert(newUser);
            if(createdAppUser != null){
                response.setStatus(HttpStatus.OK.value());
            }else{
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        }
    }

    @DeleteMapping("/delete-user")
    public void deleteManager(HttpServletRequest request, HttpServletResponse response,@RequestBody EmailIDJSON emailIDJSON){
        appUserService.deleteAppUserByEmailID(emailIDJSON.getEmailID());
        response.setStatus(HttpStatus.OK.value());
    }

    @PostMapping("/register-delivery-person")
    public void createDeliveryPerson(HttpServletResponse response, @RequestBody NewManagerJSON newDeliveryPersonJSON) {
        AppUser user = appUserService.findUserByEmailID(newDeliveryPersonJSON.getEmailID());
        if(user != null){
            response.setHeader("error", "User already exists");
            response.setStatus(HttpStatus.NOT_MODIFIED.value());
        }else{
            AppUser newUser = new AppUser();
            newUser.setUsername(newDeliveryPersonJSON.getUsername());
            newUser.setEmailID(newDeliveryPersonJSON.getEmailID());
            newUser.setPassword(newDeliveryPersonJSON.getPassword());
            newUser.setRole(roleService.findRoleByName("ROLE_DELIVERY_PERSON"));
            newUser.setPhoneNumber(newDeliveryPersonJSON.getPhoneNumber());
            newUser.setAddress(new Address());
            newUser.setWalletAmount(0);
            AppUser createdAppUser = appUserService.insert(newUser);
            if(createdAppUser != null){
                response.setStatus(HttpStatus.OK.value());
            }else{
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        }
    }

    @GetMapping("/customer")
    public ResponseEntity<AppUser> getCustomerDetails(HttpServletResponse response, @RequestBody EmailIDJSON emailIDJSON){
        AppUser appUser = appUserService.findUserByEmailID(emailIDJSON.getEmailID());
        if(appUser != null){
            return new ResponseEntity<>(appUser, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/manager/all")
    public ResponseEntity<List<AppUser>> getAllManagers(){
        List<AppUser> managerList = appUserService.findAllManagers();
        if(managerList != null){
            return new ResponseEntity<>(managerList, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/customers/all")
    public ResponseEntity<List<AppUser>> getAllCustomers(){
        List<AppUser> customerList = appUserService.findAllCustomers();
        if(customerList != null){
            return new ResponseEntity<>(customerList, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/delivery-person/all")
    public ResponseEntity<List<AppUser>> getAllDeliveryPersons(){
        List<AppUser> deliveryPersonList = appUserService.findAllDeliveryPersons();
        if(deliveryPersonList != null){
            return new ResponseEntity<>(deliveryPersonList, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }


}

@Data
class EmailIDJSON{
    private String emailID;
}
@Data
class NewManagerJSON {
    private String username;
    private String emailID;
    private String phoneNumber;
    private String password;
}

@Data
class DateJSON {
    private Date date;
}

@Data
@AllArgsConstructor
class ReportJSON {
    String productName;
    int quantity;
}
@Data
@AllArgsConstructor
class ProductSalesOnDate{
    Product product;
    int count;
}