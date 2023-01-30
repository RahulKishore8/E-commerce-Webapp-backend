package com.group60.FirstCopyFlipkart.product;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins = "localhost:3000")
public interface ProductRepository
        extends MongoRepository<Product, String> {
    //access

    Product findProductById (String productID);
    Product findProductByProductName(String productName);

    List<Product> findProductByProductNameContainingIgnoreCase(String searchString);
    List<Product> findAll();
    List<Product> findProductByCategoryNameIgnoreCase(String CategoryName);
    //modify
    @Override
    Product save(Product product);

    //delete
    void deleteProductByProductName(String name);
}
