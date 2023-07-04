package com.sparta.jummechu.controller;

import com.sparta.jummechu.dto.*;
import com.sparta.jummechu.jwt.JwtUtil;
import com.sparta.jummechu.security.UserDetailsImpl;
import com.sparta.jummechu.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    //회원가입,
    private final UserService userService;
    private final JwtUtil jwtUtil;


    @GetMapping("/loginform")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/signupform")
    public String signForm() {
        return "signup";
    }


    //회원가입
    @PostMapping("/signup")
    public String signup(@Valid SignupRequestDto requestDto) {

        try{
            userService.signup(requestDto);
        }catch (IllegalArgumentException e){
            return "redirect:/api/user/signupform";
        }

        return "redirect:/";

    }

    //로그인
    @PostMapping("/login")
    public String login(@Valid LoginRequestDto requestDto,HttpServletResponse response) {
        try {
            userService.login(requestDto);
        } catch (Exception e) {
            return "redirect:/api/user/loginform";
        }
        //login()에서 에러가 없으면 실행

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(requestDto.getUser_id(),userService.findUser(requestDto.getUser_id()).getRole()));
            return "redirect:/";
    }

    //프로필 보기
    @GetMapping("/profile")
    public List<ProfileResponseDto> getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return userService.getProfile(userDetails.getUser());
    }


    //프로필 수정
    @PutMapping("/profile")
    public ResponseEntity<ApiResponseDto> updateProfile(@Valid ProfileRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        try{
            userService.updateProfile(requestDto,userDetails.getUser());
            return ResponseEntity.ok().body(new ApiResponseDto("수정 완료", HttpStatus.OK.value()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("수정이 실패하였습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }


    //비밀번호 수정
    @PutMapping("/password")
    public ResponseEntity<ApiResponseDto> updatePassword(@Valid PasswordRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        try{
            userService.updatePassword(requestDto,userDetails.getUser());
            return ResponseEntity.ok().body(new ApiResponseDto("수정 완료", HttpStatus.OK.value()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("수정할 비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }





    //로그아웃 _ 어려워ㅠ 마지막에 하기




}

