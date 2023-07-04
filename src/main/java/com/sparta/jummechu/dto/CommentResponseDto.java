package com.sparta.jummechu.dto;

import com.sparta.jummechu.entity.Comment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class CommentResponseDto {

    private Long comment_id;
    private String content;
    private String user_id;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public CommentResponseDto(Comment comment) {
        this.comment_id = comment.getComment_id();
        this.content = comment.getContent();
        this.user_id = comment.getUser().getUser_id();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }


}
