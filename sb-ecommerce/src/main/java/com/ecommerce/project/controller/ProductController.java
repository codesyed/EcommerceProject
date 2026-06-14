package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@PathVariable Long categoryId, @Valid @RequestBody ProductDTO productDTO){
        ProductDTO savedProductDTO = productService.addProduct(categoryId, productDTO);
         return new ResponseEntity<>(savedProductDTO, HttpStatus.CREATED); //StatusCode for CREATED ?
    }
    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getProducts(
            @RequestParam(required = false, defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(required = false, defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(required = false, defaultValue = AppConstants.SORT_PRODUCT_BY) String sortBy,
            @RequestParam(required = false, defaultValue = AppConstants.SORT_ORDER) String sortOrder
    ){
        ProductResponse productResponse = productService.getProducts(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }
    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(required=false, defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(required=false, defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(required = false, defaultValue = AppConstants.SORT_PRODUCT_BY) String sortBy,
            @RequestParam(required = false, defaultValue = AppConstants.SORT_ORDER) String sortOrder
    ){
        ProductResponse productResponse = productService.getProductsByCategory(categoryId, pageNumber, pageSize, sortBy, sortOrder );
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }
    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(
            @PathVariable String keyword,
            @RequestParam(required=false, defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(required=false, defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(required = false, defaultValue = AppConstants.SORT_PRODUCT_BY) String sortBy,
            @RequestParam(required = false, defaultValue = AppConstants.SORT_ORDER) String sortOrder
    ){
        ProductResponse productResponse = productService.getProductsByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @PutMapping("/products/{productId}") //adim should have this access n?
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long productId,
                                                           @RequestBody ProductDTO productDTO){

        ProductDTO savedProductDTO = productService.updateProduct(productId, productDTO);
        return new ResponseEntity<>(savedProductDTO, HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){
        System.out.println("**BEFORE DELETE**");
        ProductDTO savedProductDTO = productService.deleteService(productId);
        System.out.println("**AFTER DELETE**");
        return new ResponseEntity<>(savedProductDTO, HttpStatus.OK);
    }

    @PutMapping("/products/{productId}/image") //adim should have this access n?
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId,
                                                           @RequestParam("image") MultipartFile image) throws IOException {
        ProductDTO savedProductDTO = productService.updateProductImage(productId, image);
        return new ResponseEntity<>(savedProductDTO, HttpStatus.OK);
    }



}
