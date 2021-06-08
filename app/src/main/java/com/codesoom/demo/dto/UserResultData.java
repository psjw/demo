package com.codesoom.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@AllArgsConstructor
public class UserResultData {
    private Long id;

    @NotBlank
    private String email;

    @NotBlank
    private String name;
}
