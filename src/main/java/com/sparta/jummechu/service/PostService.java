package com.sparta.jummechu.service;

import com.sparta.jummechu.dto.PostListResponseDto;
import com.sparta.jummechu.dto.PostRequestDto;
import com.sparta.jummechu.dto.PostResponseDto;
import com.sparta.jummechu.entity.Post;
import com.sparta.jummechu.entity.User;
import com.sparta.jummechu.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.RejectedExecutionException;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;


    public List<PostListResponseDto> getPost() {
        //db에 있는 데이터들 List형태로 보내기
        List<Post> list = postRepository.findAllByOrderByModifiedAtDesc();
        List<PostListResponseDto> postList = list.stream().map(PostListResponseDto::new).toList();
        return postList;
    }


    //해당 번호에 맞는 글 찾기
    public PostListResponseDto getPostById(Long id) {
        Post post = findContent(id);
        return new PostListResponseDto(post);
    }


    public Post findContent(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 포스트가 존재하지 않습니다."));
    }


    //작성
    @Transactional
    public PostResponseDto writePost(PostRequestDto requestDto, User user) {
        Post post = postRepository.save(new Post(requestDto, user));
        return new PostResponseDto(post);
    }


    //삭제
    @Transactional
    public void deletePost(Long id, User user) {

        //db의 user와 넘어온 user가 같다면 삭제 진행해 and 권한이 ADMIN이면 무조건 ok
        String user_id = findContent(id).getUser().getUser_id(); //기존 db의 id


        if (!(user_id.equals(user.getUser_id()) || user.getRole().toString().equals("ADMIN"))) {
            throw new RejectedExecutionException();
        }
        postRepository.deleteById(id);


    }


    //수정
    @Transactional
    public void updatePost(Long id, PostRequestDto requestDto, User user) {

        //넘어온 id에 맞는 게시글 찾기
        Post post = findContent(id);
        String user_id = findContent(id).getUser().getUser_id(); //기존 db의 id


        if (!(user_id.equals(user.getUser_id()) || user.getRole().toString().equals("ADMIN"))) {
            throw new RejectedExecutionException();
        }

        post.update(requestDto);


    }


}

