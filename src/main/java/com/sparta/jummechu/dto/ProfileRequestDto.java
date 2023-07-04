package com.sparta.jummechu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileRequestDto {
    //유저 이름
    @NotBlank
    private String username;

    //자기소개
    @NotBlank
    @Size(min = 5, max = 20)
    private String introduction;
}
