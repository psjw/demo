package com.codesoom.demo.controllers;

import com.codesoom.demo.application.ProductService;
import com.codesoom.demo.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class) //스프링 부트 전체를 테스트하는게 아닌 필요한 부분만 띄워 사용
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductController productController;

    @MockBean
    private ProductService productService;

    @BeforeEach
    void setUp() {
        Product product = new Product("쥐돌이", "냥이월드", 5000);

        given(productService.getProducts()).willReturn(List.of(product));
        given(productService.getProduct(1L)).willReturn(product);
        given(productService.getProduct(1000L)).willThrow(new ProductNotFoundException(1000L));
        given(productService.createProduct(any(Product.class))).willReturn(product);
        given(productService.updateProduct(eq(1L), any(Product.class))).will(invocation -> {
            Product source = invocation.getArgument(1);
            Long id = invocation.getArgument(0);
            return new Product(id, source.getName(), source.getMaker(), source.getPrice());
        });
        given(productService.updateProduct(eq(1000L),any(Product.class)))
                .willThrow(new ProductNotFoundException(1000L));
        given(productService.deleteProduct(eq(1000L)))
                .willThrow(new ProductNotFoundException(1000L));
    }

    @Test
    void list() throws Exception {
        //상품 목록을 이미 갖추고 있음
        mockMvc.perform(get("/products")
                .accept(MediaType.APPLICATION_JSON_UTF8)) //WebMVC는 UTF-8을 기본으로 안잡음
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("쥐돌이")));
    }

    @Test
    void detailWithExsitedProduct() throws Exception {
        mockMvc.perform(get("/products/1")
                .accept(MediaType.APPLICATION_JSON_UTF8)) //WebMVC는 UTF-8을 기본으로 안잡음
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("쥐돌이")));
    }


    @Test
    void detailWithNotExsitedProduct() throws Exception {
        mockMvc.perform(get("/products/1000")
                .accept(MediaType.APPLICATION_JSON_UTF8)) //WebMVC는 UTF-8을 기본으로 안잡음
                .andExpect(status().isNotFound());
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(post("/products")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\",\"price\":5000}")) //WebMVC는 UTF-8을 기본으로 안잡음
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("쥐돌이")));
        verify(productService).createProduct(any(Product.class));
    }

    @Test
    void updateWithExistedProduct() throws Exception {
        mockMvc.perform(patch("/products/1")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\",\"price\":5000}")) //WebMVC는 UTF-8을 기본으로 안잡음
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("쥐순이")));
        verify(productService).updateProduct(eq(1L), any(Product.class));
    }

    @Test
    void updateWithNotExistedProduct() throws Exception {
        mockMvc.perform(patch("/products/1000")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\",\"price\":5000}")) //WebMVC는 UTF-8을 기본으로 안잡음
                .andExpect(status().isNotFound());
    }


    @Test
    void destoryWithExistedProduct() throws Exception {
        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isOk());
        verify(productService).deleteProduct(eq(1L));
    }

    @Test
    void destoryWithNotExistedProduct() throws Exception {
        mockMvc.perform(delete("/products/1000"))
                .andExpect(status().isNotFound());
        verify(productService).deleteProduct(eq(1000L));
    }

}