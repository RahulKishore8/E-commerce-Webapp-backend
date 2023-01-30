package com.group60.FirstCopyFlipkart.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    @GetMapping("/get-one")
    public Product getOneProduct(HttpServletRequest request){
        String id = request.getParameter("id");
        return productService.findProductByProductID(id);
    }
    @GetMapping("/get-category")
    public List<Product> getProductsByCategory(HttpServletRequest request) {
        String category = request.getParameter("category");
        return productService.findProductByCategoryName(category);
    }
    @GetMapping("/search")
    public List<Product> searchProductsByString(HttpServletRequest request){
        String searchString = request.getParameter("searchString");
        return productService.searchProduct(searchString);
    }
//    @PostMapping("/add-or-edit")
//    public ResponseEntity<Product> addOrEditProduct(@RequestBody Product newProduct) {
//        Product product = productService.save(newProduct);
//        if(product == null){
//            System.out.println("Error while inserting");
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }else{
//            return new ResponseEntity<>(product, HttpStatus.CREATED);
//        }
//    }
    @PostMapping("/add-or-edit")
    public ResponseEntity<Product> addOrEditProduct(@RequestBody Product newProduct) {
        Product product = productService.findProductByProductName(newProduct.getProductName());
        if(product == null){
            productService.save(newProduct);
            return new ResponseEntity<>(newProduct,HttpStatus.OK);
        }else {
            product.setQuantity(newProduct.getQuantity());
            product.setDiscount(newProduct.getDiscount());
            product.setPrice(newProduct.getPrice());
            product.setCategoryName(newProduct.getCategoryName());
            product.setDeliveryTime(newProduct.getDeliveryTime());
            product.setImageURL(newProduct.getImageURL());
            Product savedProduct = productService.save(product);
            if (savedProduct == null) {
                System.out.println("Error while updating");
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
            }
        }
    }
    @DeleteMapping("/delete")
    public ResponseEntity<Product> deleteProduct(@RequestBody NameJSON nameJSON){
        productService.deleteByName(nameJSON.getName());
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}

@Data
class NameJSON {
    String name;
}
@Data
class CategoryJSON {
    String category;
}

@Data
class SearchJSON {
    String searchString;
}

@Data
class ProductIDJSON{
    String id;
}