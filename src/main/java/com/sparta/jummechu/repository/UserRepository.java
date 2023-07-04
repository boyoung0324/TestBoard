package com.sparta.jummechu.repository;

import com.sparta.jummechu.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findById(String id);
}

