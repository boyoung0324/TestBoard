package com.sparta.jummechu.dto;

import com.sparta.jummechu.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostListResponseDto {
    private Long post_id;
    private String title;
    private String content;
    private String user_id;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponseDto> commentList;

    public PostListResponseDto(Post post) {
        this.post_id = post.getPost_id();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.user_id = post.getUser().getUser_id();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.commentList = post.getCommentList().stream()
                .map(CommentResponseDto::new)
                .sorted(Comparator.comparing(CommentResponseDto::getCreatedAt).reversed())
                .collect(Collectors.toList());


    }





}
