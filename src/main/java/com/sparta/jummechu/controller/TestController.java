package com.sparta.jummechu.controller;

import com.sparta.jummechu.dto.ApiResponseDto;
import com.sparta.jummechu.dto.PostListResponseDto;
import com.sparta.jummechu.dto.PostRequestDto;
import com.sparta.jummechu.dto.PostResponseDto;
import com.sparta.jummechu.security.UserDetailsImpl;
import com.sparta.jummechu.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
public class TestController {

    private final PostService postService;

    //전체 조회
    @GetMapping("/post")
    public List<PostListResponseDto> getPost(Model model) {


        return postService.getPost();
    }


    //선택 조회
    @GetMapping("/post/{id}")
    public PostListResponseDto getPostById(@PathVariable Long id) {
        return postService.getPostById(id);

    }


    //작성
    @PostMapping("/post")
    public PostResponseDto writePost(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.writePost(requestDto, userDetails.getUser());
    }

    //삭제
    @DeleteMapping("/post/{id}")
    public ResponseEntity<ApiResponseDto> deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            postService.deletePost(id, userDetails.getUser());
            return ResponseEntity.ok().body(new ApiResponseDto("삭제 완료", HttpStatus.OK.value()));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("작성자만 삭제할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
        }

    }

    //    수정
    @PutMapping("/post/{id}")
    public ResponseEntity<ApiResponseDto> updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            postService.updatePost(id, requestDto, userDetails.getUser());
            return ResponseEntity.ok().body(new ApiResponseDto("수정 완료", HttpStatus.OK.value()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("작성자만 수정할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }


}
