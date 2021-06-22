package com.codesoom.demo.controllers;

import com.codesoom.demo.application.AuthenticationService;
import com.codesoom.demo.application.ProductService;
import com.codesoom.demo.domain.Product;
import com.codesoom.demo.domain.Role;
import com.codesoom.demo.dto.ProductData;
import com.codesoom.demo.errors.InvalidTokenException;
import com.codesoom.demo.errors.ProductNotFoundException;
import com.codesoom.demo.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
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

    @MockBean
    private ProductService productService;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private JwtUtil jwtUtil;

    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2" +
            "VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";

    private static final String INVALID_TOKEN = VALID_TOKEN + "WRONG";

    @BeforeEach
    void setUp() {
        Product product = Product.builder()
                .id(1L)
                .name("쥐돌이")
                .maker("냥이월드")
                .price(5000)
                .build();

        given(productService.getProducts()).willReturn(List.of(product));
        given(productService.getProduct(1L)).willReturn(product);
        given(productService.getProduct(1000L)).willThrow(new ProductNotFoundException(1000L));
        given(productService.createProduct(any(ProductData.class))).willReturn(product);
        given(productService.updateProduct(eq(1L), any(ProductData.class))).will(invocation -> {
            ProductData source = invocation.getArgument(1);
            Long id = invocation.getArgument(0);
            return Product.builder()
                    .id(id)
                    .name(source.getName())
                    .maker(source.getMaker())
                    .price(source.getPrice())
                    .build();
        });
        given(productService.updateProduct(eq(1000L), any(ProductData.class)))
                .willThrow(new ProductNotFoundException(1000L));
        given(productService.deleteProduct(eq(1000L)))
                .willThrow(new ProductNotFoundException(1000L));

        given(jwtUtil.decode(any())).will(
                invocation -> {
                    String token = invocation.getArgument(0);
                    return new JwtUtil("12345678901234567890123456789012")
                            .decode(token);
                });

        given(authenticationService.parseToken(VALID_TOKEN)).willReturn(1L);

        given(authenticationService.parseToken(INVALID_TOKEN))
                .willThrow(new InvalidTokenException(INVALID_TOKEN));

        given(authenticationService.roles(1L)).willReturn(Arrays.asList(new Role("USER")));
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
    void createWithValidAttributes() throws Exception {
        mockMvc.perform(post("/products")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\",\"price\":5000}")
                .header("Authorization", "Bearer " + VALID_TOKEN)) //WebMVC는 UTF-8을 기본으로 안잡음
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("쥐돌이")));
        verify(productService).createProduct(any(ProductData.class));
    }

    @Test
    void createWithInvalidAttributes() throws Exception {
        mockMvc.perform(post("/products")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"\",\"maker\":\"\",\"price\":0}")
                .header("Authorization", "Bearer " + VALID_TOKEN)) //WebMVC는 UTF-8을 기본으로 안잡음
                .andExpect(status().isBadRequest());
    }


    @Test
    void createWithAccessToken() throws Exception {
        mockMvc.perform(post("/products")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\",\"price\":5000}") //WebMVC는 UTF-8을 기본으로 안잡음
                .header("Authorization", "Bearer " + VALID_TOKEN)) //WebMVC는 UTF-8을 기본으로 안잡음
                .andExpect(status().isCreated());
    }

    @Test
    void createWithoutAccessToken() throws Exception {
        mockMvc.perform(post("/products")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\",\"price\":5000}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createWithWrongAccessToken() throws Exception {
        mockMvc.perform(post("/products")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"쥐돌이\",\"maker\":\"냥이월드\",\"price\":5000}")
                .header("Authorization", "Bearer " + INVALID_TOKEN))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateWithExistedProduct() throws Exception {
        mockMvc.perform(patch("/products/1")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + VALID_TOKEN)
                .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\",\"price\":5000}")) //WebMVC는 UTF-8을 기본으로 안잡음
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("쥐순이")));
        verify(productService).updateProduct(eq(1L), any(ProductData.class));
    }

    @Test
    void updateWithoutAcccessToken() throws Exception {
        mockMvc.perform(patch("/products/1")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\",\"price\":5000}")) //WebMVC는 UTF-8을 기본으로 안잡음
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateInvalidAcccessToken() throws Exception {
        mockMvc.perform(patch("/products/1")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + INVALID_TOKEN)
                .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\",\"price\":5000}")) //WebMVC는 UTF-8을 기본으로 안잡음
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateWithNotExistedProduct() throws Exception {
        mockMvc.perform(patch("/products/1000")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + VALID_TOKEN)
                .content("{\"name\":\"쥐순이\",\"maker\":\"냥이월드\",\"price\":5000}")) //WebMVC는 UTF-8을 기본으로 안잡음
                .andExpect(status().isNotFound());
    }

    @Test
    void updateWithNotInvalidAttributes() throws Exception {
        mockMvc.perform(patch("/products/1")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + VALID_TOKEN)
                .content("{\"name\":\"\",\"maker\":\"\",\"price\":5000}")) //WebMVC는 UTF-8을 기본으로 안잡음
                .andExpect(status().isBadRequest());
    }


    @Test
    void destoryWithExistedProduct() throws Exception {
        mockMvc.perform(delete("/products/1")
                .header("Authorization", "Bearer " + VALID_TOKEN))
                .andExpect(status().isOk());
        verify(productService).deleteProduct(eq(1L));
    }

    @Test
    void destoryWithNotExistedProduct() throws Exception {
        mockMvc.perform(delete("/products/1000")
                .header("Authorization", "Bearer " + VALID_TOKEN))
                .andExpect(status().isNotFound());
        verify(productService).deleteProduct(eq(1000L));
    }


    @Test
    void destoryWithOutAccessToken() throws Exception {
        mockMvc.perform(delete("/products/1000"))
                .andExpect(status().isUnauthorized());
    }

}