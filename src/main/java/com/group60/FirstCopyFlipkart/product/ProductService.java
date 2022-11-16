package com.group60.FirstCopyFlipkart.product;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;

    //access
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product findProductByProductID(String productID) {
        return productRepository.findProductByProductID(productID);
    }

    public Product findProductByProductName(String productName) {
        return productRepository.findProductByProductName(productName);
    }

    //modify
    public Product save(Product newProduct){
        return productRepository.save(newProduct);
    }

    public Product updateQuantity(String productID, int newQuantity){
        Product product = productRepository.findProductByProductID(productID);
        if(product != null){
            product.setQuantity(newQuantity);
            return save(product);
        }else{
            return null;
        }
    }

    //delete
    public void delete(Product product){
        productRepository.deleteById(product.getProductID());
    }
}
