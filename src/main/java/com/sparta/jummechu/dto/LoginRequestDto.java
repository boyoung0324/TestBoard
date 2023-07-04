package com.sparta.jummechu.dto;

import com.sparta.jummechu.entity.UserRoleEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequestDto {
    @NotBlank
    private String user_id;
    @NotBlank
    private String password;

}
