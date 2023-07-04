package com.sparta.jummechu.controller;

import com.sparta.jummechu.dto.ApiResponseDto;
import com.sparta.jummechu.dto.PostListResponseDto;
import com.sparta.jummechu.dto.PostRequestDto;
import com.sparta.jummechu.dto.PostResponseDto;
import com.sparta.jummechu.security.UserDetailsImpl;
import com.sparta.jummechu.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class PostController {

    private final PostService postService;


    //    ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    @GetMapping("/post") //board의 첫 화면 게시물 목록 --> getPage
    public String list(Model m) {

        try {

            List<PostListResponseDto> postList = postService.getPost();
            m.addAttribute("postList", postList);
            return "boardList"; // 로그인을 한 상태이면, 게시판 화면으로 이동

        } catch (Exception e) {
            return "redirect:/";
        }


    }

//    ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    //전체 조회
//    @GetMapping("/post")
//    public List<PostListResponseDto> getPost() {
//        return postService.getPost();
//    }


    //선택 조회
    @GetMapping("/post/{id}")
    public String getPostById(@PathVariable Long id,Model model) {
        PostListResponseDto postList =  postService.getPostById(id);
        model.addAttribute("postList",postList);
        return "board";

    }


    @GetMapping("/post/write")
    public String write(Model model) { //작성할 때 user값이 안 들어옴

        return "writePost";
    }


    //작성
    @PostMapping("/post")
    public String writePost(PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails,Model model) {
        PostResponseDto responseDto = postService.writePost(requestDto, userDetails.getUser());
        model.addAttribute("post",responseDto);

        return "writePost";
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
