package com.group60.FirstCopyFlipkart.appUser;

import com.group60.FirstCopyFlipkart.Role.Role;
import com.group60.FirstCopyFlipkart.Role.RoleRepository;
import com.group60.FirstCopyFlipkart.product.Product;
import com.group60.FirstCopyFlipkart.product.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class AppUserService implements UserDetailsService {
    private final AppUserRepository appUserRepository;
    private final ProductRepository productRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String emailID) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findAppUserByEmailID(emailID);
        if(appUser == null) {
            log.error("User not found");
            throw new UsernameNotFoundException("User not found in database");
        }else{
            log.info("User found in database: {}", emailID);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(appUser.getRole().getName()));
        return new User(appUser.getEmailID(), appUser.getPassword(), authorities);
    }
    //Access
    public List<AppUser> findAllManagers(){
        Role manager = roleRepository.findRoleByName("ROLE_MANAGER");
        return appUserRepository.findAllByRole(manager);
    }

    public List<AppUser> findAllCustomers(){
        Role customer = roleRepository.findRoleByName("ROLE_CUSTOMER");
        return appUserRepository.findAllByRole(customer);
    }

    public List<AppUser> findAllDeliveryPersons(){
        Role customer = roleRepository.findRoleByName("ROLE_DELIVERY_PERSON");
        return appUserRepository.findAllByRole(customer);
    }
    public HttpStatus updateOrderStatus(String emailID, String orderID, String status){
        AppUser appUser = appUserRepository.findAppUserByEmailID(emailID);
        if(appUser == null){
            return HttpStatus.NOT_FOUND;
        }else{
            ArrayList<Order> orderList = appUser.getOrderList();
            for (Order order : orderList) {
                if (order.getId().equals(orderID)) {
                    order.setStatus(status);
                }
            }
            appUser.setOrderList(orderList);
            AppUser savedUser = appUserRepository.save(appUser);
            if(savedUser == null){
                return HttpStatus.NOT_MODIFIED;
            }else{
                return HttpStatus.OK;
            }
        }
    }
    public AppUser findUserByEmailID(String emailID){
        return appUserRepository.findAppUserByEmailID(emailID);
    }

    //Insert
    public AppUser insert(AppUser appUser){
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        return appUserRepository.insert(appUser);
    }
    public AppUser save(AppUser appUser){
        return appUserRepository.save(appUser);
    }

    //Change Password
    public AppUser changePassword(String emailID, String newPassword){
        AppUser appUser = findUserByEmailID(emailID);
        if(appUser != null){
            appUser.setPassword(passwordEncoder.encode(newPassword));
            return appUserRepository.save(appUser);
        } else{
            return null;
        }

    }

    //Change Address
    public AppUser changeAddress(String emailID, Address newAddress){
        AppUser appUser = findUserByEmailID(emailID);
        if(appUser != null){
            appUser.setAddress(newAddress);
            return appUserRepository.save(appUser);
        } else{
            return null;
        }
    }

    //Change PhoneNumber
    public AppUser changePhoneNumber(String emailID, String newPhoneNumber){
        AppUser appUser = findUserByEmailID(emailID);
        if(appUser != null){
            appUser.setPhoneNumber(newPhoneNumber);
            return appUserRepository.save(appUser);
        } else{
            return null;
        }
    }

    //Change EmailID
    public AppUser changeEmailID(String emailID, String newEmailID){
        AppUser appUser = findUserByEmailID(emailID);
        if(appUser != null){
            appUser.setPhoneNumber(newEmailID);
            return appUserRepository.save(appUser);
        } else{
            return null;
        }
    }

    //Change Username
    public AppUser changeUsername(String emailID, String newUsername){
        AppUser appUser = findUserByEmailID(emailID);
        if(appUser != null){
            appUser.setUsername(newUsername);
            return appUserRepository.save(appUser);
        } else{
            return null;
        }
    }
    //Add Balance
    public AppUser addBalance(String emailID, int addedBalance){
        AppUser appUser = findUserByEmailID(emailID);
        if(appUser != null){
            appUser.setWalletAmount(appUser.getWalletAmount() + addedBalance);
            return appUserRepository.save(appUser);
        } else{
            return null;
        }
    }
    //Delete User
    public void deleteAppUserByEmailID(String emailID){
        appUserRepository.deleteAppUserByEmailID(emailID);
    }

    public int getCartTotalPrice(String emailID){
        AppUser appUser = appUserRepository.findAppUserByEmailID(emailID);
        int totalPrice = 0;
        ArrayList<CartItem> itemList = appUser.getCart().getItemList();
        for (CartItem cartItem : itemList) {
            String productID = cartItem.getProductID();
            Product product = productRepository.findProductByProductID(productID);
            float productPrice = product.getPrice();
            totalPrice += cartItem.getQuantity() * productPrice;
        }
        return totalPrice;
    }
    public HttpStatus placeOrder(String emailID){
        AppUser user = appUserRepository.findAppUserByEmailID(emailID);
        int totalPrice = getCartTotalPrice(emailID);
        if(user.getWalletAmount() >= totalPrice){
            ArrayList<CartItem> itemList = user.getCart().getItemList();
            for (CartItem cartItem : itemList) {
                String productID = cartItem.getProductID();
                Product product = productRepository.findProductByProductID(productID);
                product.setOrderCount(product.getOrderCount() + cartItem.getQuantity());
                product.setQuantity(product.getQuantity() - cartItem.getQuantity());
                productRepository.save(product);
            }
            user.setWalletAmount(user.getWalletAmount() - totalPrice);
            user.getOrderList().add(new Order(user.getId() + "_" + Integer.toString(user.getOrderList().size()), user.getCart(),new Date(), "order placed"));
            user.getCart().setItemList(new ArrayList<>());
            appUserRepository.save(user);
            return(HttpStatus.OK);
        }else{
            return(HttpStatus.NOT_MODIFIED);
        }
    }
}
