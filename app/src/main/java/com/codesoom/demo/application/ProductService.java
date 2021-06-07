//1. getProducts -> 목록
//2.  getProduct -> 상세정보
//3. createProduct -> 상품추가
//4. updateProduct -> 상품 수정
//5. deleteProduct -> 상품 삭제
package com.codesoom.demo.application;

import com.codesoom.demo.controllers.ProductNotFoundException;
import com.codesoom.demo.domain.Product;
import com.codesoom.demo.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts() {
        //실제로 구현할것
        return productRepository.findAll();
    }

    public Product getProduct(Long id) {
        return findProduct(id);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product source) {
        //product 얻기
        Product product = findProduct(id);
        //정보바꾸기
        product.change(source);
        return product;
    }

    public Product deleteProduct(Long id) {
        Product product = findProduct(id);
        productRepository.delete(product);
        return product;
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
