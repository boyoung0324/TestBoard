package com.sparta.jummechu.controller;

import com.sparta.jummechu.dto.ApiResponseDto;
import com.sparta.jummechu.dto.CommentRequestDto;
import com.sparta.jummechu.dto.CommentResponseDto;
import com.sparta.jummechu.security.UserDetailsImpl;
import com.sparta.jummechu.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {
    //댓글
    private final CommentService commentService;


    //댓글 작성
    @PostMapping("/comment")
    public CommentResponseDto write(@RequestBody CommentRequestDto requestDto, @RequestParam Long post_id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.writeComment(requestDto, userDetails.getUser(), post_id);
    }

    //댓글 삭제
    @DeleteMapping("/comment/{id}")
    public ResponseEntity<ApiResponseDto> delete(@PathVariable Long id, @RequestParam Long post_id, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        try {
            commentService.deleteComment(id, post_id, userDetails.getUser());
            return ResponseEntity.ok().body(new ApiResponseDto("삭제 완료", HttpStatus.OK.value()));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("작성자만 삭제할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
        }


    }

    //댓글 수정
    @PutMapping("/comment/{id}")
    public ResponseEntity<ApiResponseDto> update(@PathVariable Long id, @RequestParam Long post_id, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        try {
            commentService.updateCommnet(id, post_id, requestDto, userDetails.getUser());
            return ResponseEntity.ok().body(new ApiResponseDto("수정 완료", HttpStatus.OK.value()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("작성자만 수정할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }


}
