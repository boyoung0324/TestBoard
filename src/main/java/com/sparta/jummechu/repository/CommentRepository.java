package com.sparta.jummechu.repository;


import com.sparta.jummechu.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {


}
