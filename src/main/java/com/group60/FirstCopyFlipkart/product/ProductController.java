package com.group60.FirstCopyFlipkart.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/product")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/all")
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }
    @GetMapping("/get-category")
    public List<Product> getProductsByCategory(@RequestBody CategoryJSON categoryJSON) {
        return productService.findProductByCategoryName(categoryJSON.getCategory() );
    }
    @GetMapping("/search")
    public List<Product> searchProductsByString(@RequestBody SearchJSON searchJSON){
        return productService.searchProduct(searchJSON.getSearchString());
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

@Data
class CategoryJSON {
    String category;
}

@Data
class SearchJSON {
    String searchString;
}
