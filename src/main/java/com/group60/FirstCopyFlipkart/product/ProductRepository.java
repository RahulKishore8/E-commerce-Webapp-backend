package com.group60.FirstCopyFlipkart.product;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository
        extends MongoRepository<Product, String> {
    //access

    Product findProductByProductID(String productID);

    Product findProductByProductName(String productName);

    List<Product> findAll();

    //modify
    @Override
    Product save(Product product);

    //delete

    @Override
    void deleteById(String s);
}
