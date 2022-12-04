package com.group60.FirstCopyFlipkart.product;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository
        extends MongoRepository<Product, String> {
    //access

    Product findProductById (String productID);
    Product findProductByProductName(String productName);

    List<Product> findProductByProductNameContainingIgnoreCase(String searchString);
    List<Product> findAll();
    List<Product> findProductByCategoryName(String CategoryName);
    //modify
    @Override
    Product save(Product product);

    //delete

    @Override
    void deleteById(String s);
}
