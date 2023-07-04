package com.sparta.jummechu.service;

import com.sparta.jummechu.dto.*;
import com.sparta.jummechu.entity.User;
import com.sparta.jummechu.entity.UserRoleEnum;
import com.sparta.jummechu.jwt.JwtUtil;
import com.sparta.jummechu.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.RejectedExecutionException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;


    private final String ADMIN_TOKEN = "ADMIN";


    @Transactional
    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String id = requestDto.getUser_id();
        String pwd = passwordEncoder.encode(requestDto.getPassword());
        String introduction = requestDto.getIntroduction();

        //아이디 중복
        Optional<User> checkUsername = repository.findById(id);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 아이디가 존재합니다.");
        }


        UserRoleEnum role;


        if ("".equals(requestDto.getRole())) { //값이 없으면 User
            role = UserRoleEnum.USER;
        } else if (!ADMIN_TOKEN.equals(requestDto.getRole())) { //if문 안으로 들어가면, 지역변수로 저장이X, 바깥에 선언해줘야 함
            throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
        } else {
            role = UserRoleEnum.ADMIN;
        }


        User user = new User(id, pwd, username, introduction, role);
        repository.save(user);


    }


    public void login(LoginRequestDto requestDto) {
        String id = requestDto.getUser_id();
        String pwd = requestDto.getPassword();

        User user = repository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다")
        );

        if (!passwordEncoder.matches(pwd, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }


    }

    public List<ProfileResponseDto> getProfile(User user) { //프로필 보기

        //로그인된 user의 id에 맞는 정보들 보여주기
        List<User> list = repository.findById(user.getUser_id()).stream().toList();
        List<ProfileResponseDto> result = list.stream().map(ProfileResponseDto::new).toList();
        return result;


    }

    @Transactional
    public ProfileResponseDto updateProfile(ProfileRequestDto requestDto, User user) { //프로필 수정(비밀번호,자기소개만 수정 가능)

        User newUser = findUser(user.getUser_id());
        newUser.setUsername(requestDto.getUsername());
//        upUser.setId(requestDto.getId());
        newUser.setIntroduction(requestDto.getIntroduction());
        return new ProfileResponseDto(newUser);


    }

    @Transactional
    public void updatePassword(PasswordRequestDto requestDto, User user) {
        User newUser = findUser(user.getUser_id());

        if (!requestDto.getNewPwd().equals(requestDto.getNewPwd2())) {
            throw new RejectedExecutionException();
        }

        newUser.setPassword(passwordEncoder.encode(requestDto.getNewPwd()));
    }

    public User findUser(String id) {
        return repository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 유저가 존재하지 않습니다.")
        );
    }


}

