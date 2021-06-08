package com.codesoom.demo.domain;
//고양이 장난감 쇼핑몰
//Product 모델
//User 모델
// Order  모델
// ... 모델
//Application (UseCase)
// Product ->  관리자 등록/수정/삭제 -> list/detail
// 주문 -> 확인 -> 배송 등 처리

//Product
// 0. 식별자 - identifier(ID)
// 1. 이름 - 쥐돌이
// 2.. 제조사 - 냥이 월드
// 3. 가격 - 5,000 원 (판매가)
// 4. 이미지 - static, CDN =>  image URL

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue
    private Long id; // final  붙이면 문제 -> 내부적으로 JPA 사용시 리플렉션을 통해  setter getter 메서드 생성하여 삽입하기때문에 안됨
    @NotBlank
    private String name;
    @NotBlank
    private String maker;
    @NotNull
    private Integer price;
    private String imageUrl;

    public Product(String name, String maker, Integer price) {
        this.name = name;
        this.maker = maker;
        this.price = price;
    }

    public void change(String name, String maker, Integer price) {
        //TODO :...
        this.name = name;
        this.maker = maker;
        this.price = price;
    }
}
