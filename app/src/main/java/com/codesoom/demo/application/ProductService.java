//1. getProducts -> 목록
//2.  getProduct -> 상세정보
//3. createProduct -> 상품추가
//4. updateProduct -> 상품 수정
//5. deleteProduct -> 상품 삭제
package com.codesoom.demo.application;

import com.codesoom.demo.errors.ProductNotFoundException;
import com.codesoom.demo.domain.Product;
import com.codesoom.demo.domain.ProductRepository;
import com.codesoom.demo.dto.ProductData;
import com.github.dozermapper.core.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductService {
    private final Mapper mapper;

    private final ProductRepository productRepository;

    public ProductService(Mapper mapper, ProductRepository productRepository) {
        this.mapper = mapper;
        this.productRepository = productRepository;
    }

    public List<Product> getProducts() {
        //실제로 구현할것
        return productRepository.findAll();
    }

    public Product getProduct(Long id) {
        return findProduct(id);
    }

    public Product createProduct(ProductData productData) {

        Product product = mapper.map(productData, Product.class); //ORM setter지향하기 위해
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, ProductData productData) {
        //product 얻기
        Product product = findProduct(id);
        //정보바꾸기
        product.changeWith(mapper.map(productData, Product.class));
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
