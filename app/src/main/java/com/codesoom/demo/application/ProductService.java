//1. getProducts -> 목록
//2.  getProduct -> 상세정보
//3. createProduct -> 상품추가
//4. updateProduct -> 상품 수정
//5. deleteProduct -> 상품 삭제
package com.codesoom.demo.application;

import com.codesoom.demo.domain.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    public List<Product> getProducts() {
        //실제로 구현할것
        return null;
    }

    public Product getProduct(Long id) {
        return null;
    }

    public Product createProduct(Product product) {
        return null;
    }

    public Product updateProduct(Long l, Product source) {
        return null;
    }

    public Product deleteProduct(Long id) {
        return null;
    }
}
