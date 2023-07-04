package com.sparta.jummechu.dto;

import com.sparta.jummechu.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {
    private Long post_id;
    private String title;
    private String content;
    private String user_id;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public PostResponseDto(Post post) {
        this.post_id = post.getPost_id();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.user_id = post.getUser().getUser_id();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();

    }





}
