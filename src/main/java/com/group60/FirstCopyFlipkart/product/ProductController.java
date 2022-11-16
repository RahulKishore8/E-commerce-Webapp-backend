package com.group60.FirstCopyFlipkart.product;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    @PostMapping
    public ResponseEntity<Product> addNewProduct(@RequestBody Product newProduct) {
        Product product = productService.save(newProduct);
        if(product == null){
            System.out.println("Error while inserting");
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }else{
            return new ResponseEntity<>(product, HttpStatus.CREATED);
        }
    }
    @PatchMapping
    public ResponseEntity<Product> updateProduct(@RequestBody Product newProduct){
        Product product = productService.save(newProduct);
        if(product == null){
            System.out.println("Error while updating");
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }else{
            return new ResponseEntity<>(product, HttpStatus.CREATED);
        }
    }
    @DeleteMapping
    public ResponseEntity<Product> deleteProduct(@RequestBody Product product){
        productService.delete(product);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }
}



