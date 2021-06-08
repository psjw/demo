// REST API
//1. GET /products -> 목록
//2.  GET /products/{id} -> 상세정보
//3.  POST /products -> 상품추가
//4.  PUT/PATCH /products/{id} -> 상품 수정
//5.  DELETE /products/{id} -> 상품 삭제
package com.codesoom.demo.controllers;

import com.codesoom.demo.application.ProductService;
import com.codesoom.demo.domain.Product;
import com.codesoom.demo.dto.ProductData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> list(){
        return productService.getProducts();
//        Product product = new Product("쥐돌이", "냥이월드", 5000);
//        return List.of(product);
    }

    @GetMapping("{id}")
    public Product detail(@PathVariable Long id){
        return productService.getProduct(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@RequestBody @Valid ProductData productData){
        return productService.createProduct(productData);
    }

    @PatchMapping("{id}")
    public Product update(@PathVariable Long id, @RequestBody @Valid  ProductData productData){
        return productService.updateProduct(id, productData);
    }

    @DeleteMapping("{id}")
    public void destroy(@PathVariable Long id){
        productService.deleteProduct(id);
    }

}
