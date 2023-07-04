package com.sparta.jummechu.service;

import com.sparta.jummechu.dto.CommentRequestDto;
import com.sparta.jummechu.dto.CommentResponseDto;
import com.sparta.jummechu.entity.Comment;
import com.sparta.jummechu.entity.Post;
import com.sparta.jummechu.entity.User;
import com.sparta.jummechu.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.RejectedExecutionException;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepo;
    private final PostService postService;


    //작성
    @Transactional
    public CommentResponseDto writeComment(CommentRequestDto requestDto, User user, Long post_id) {


        //post_id에 맞는 board객체 찾기
        Post post = postService.findContent(post_id);

        //comment객체에 requestDto랑  user랑 board넣기
        Comment comment = commentRepo.save(new Comment(requestDto, user, post));


        //board의 commentList에 comment 넣기
        post.addCommentList(comment);


        return new CommentResponseDto(comment);


    }

    //삭제
    @Transactional
    public void deleteComment(Long id, Long post_id, User user) {
        String user_id = findComment(id).getUser().getUser_id();
        Post post = postService.findContent(post_id);

        if (!(user_id.equals(user.getUser_id()) || user.getRole().toString().equals("ADMIN"))) {
            throw new RejectedExecutionException();

        }
        commentRepo.deleteById(id);
        List<Comment> list = post.getCommentList();
        list.removeIf(c -> c.getComment_id() == id);


    }


    @Transactional
    public CommentResponseDto updateCommnet(Long id, Long post_id, CommentRequestDto requestDto, User user) {

        String user_id = findComment(id).getUser().getUser_id();
//        Post post = postService.findContent(post_id); //1번 게시글. 댓글 많이 달려있음
        Comment comment = findComment(id); //하나의 댓글

        //db수정 & commentList도 수정해야 한다

        if (!(user_id.equals(user.getUser_id()) || user.getRole().toString().equals("ADMIN"))) {
            throw new RejectedExecutionException();

        }

//            List<Comment> list = post.getCommentList();
            comment.update(requestDto);

            return new CommentResponseDto(comment);


    }


    //댓글 찾기
    public Comment findComment(Long id) {
        return commentRepo.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 메모가 존재하지 않습니다."));
    }

}
