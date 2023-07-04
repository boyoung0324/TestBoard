package com.sparta.jummechu.dto;

import com.sparta.jummechu.entity.User;
import lombok.Getter;

@Getter
public class ProfileResponseDto {
    //아이디
    //이름
    //자기소개
    private String user_id;
    private String username;
    private String introduction;

    public ProfileResponseDto(User user) {
        this.user_id = user.getUser_id();
        this.username = user.getUsername();
        this.introduction = user.getIntroduction();
    }

}
