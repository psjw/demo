package com.codesoom.demo.dto;

//외부랑 소통

import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductData {

    private Long id;

    @NotBlank
    @Mapping("name")
    private String name;

    @NotBlank
    @Mapping("maker")
    private String maker;

    @NotNull
    @Mapping("price")
    private Integer price;

    @Mapping("imageUrl")
    private String imageUrl;
}
