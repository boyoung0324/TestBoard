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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    //회원가입,
    private final UserService userService;
    private final JwtUtil jwtUtil;


    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto> signup(@RequestBody @Valid SignupRequestDto requestDto) {

        try{
            userService.signup(requestDto);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(new ApiResponseDto("중복된 id 입니다.", HttpStatus.BAD_REQUEST.value()));
        }

        return ResponseEntity.ok().body(new ApiResponseDto("회원가입 성공", HttpStatus.CREATED.value()));

    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto> login(@RequestBody @Valid LoginRequestDto requestDto,HttpServletResponse response) {
        try {
            userService.login(requestDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("회원을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST.value()));
        }
        //login()에서 에러가 없으면 실행

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(requestDto.getUser_id(),userService.findUser(requestDto.getUser_id()).getRole()));
        return ResponseEntity.ok().body(new ApiResponseDto("로그인 성공", HttpStatus.CREATED.value()));
    }

    //프로필 보기
    @GetMapping("/profile")
    public List<ProfileResponseDto> getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return userService.getProfile(userDetails.getUser());
    }


    //프로필 수정
    @PutMapping("/profile")
    public ResponseEntity<ApiResponseDto> updateProfile(@RequestBody @Valid ProfileRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        try{
            userService.updateProfile(requestDto,userDetails.getUser());
            return ResponseEntity.ok().body(new ApiResponseDto("수정 완료", HttpStatus.OK.value()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("수정이 실패하였습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }


    //비밀번호 수정
    @PutMapping("/password")
    public ResponseEntity<ApiResponseDto> updatePassword(@RequestBody @Valid PasswordRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        try{
            userService.updatePassword(requestDto,userDetails.getUser());
            return ResponseEntity.ok().body(new ApiResponseDto("수정 완료", HttpStatus.OK.value()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("수정할 비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }





    //로그아웃 _ 어려워ㅠ 마지막에 하기




}

