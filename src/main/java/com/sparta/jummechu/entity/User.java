package com.sparta.jummechu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user")
@NoArgsConstructor
public class User {

    @Id
    private String user_id;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "introduction", nullable = false)
    private String introduction;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;


    public User(String user_id, String password, String username, String introduction, UserRoleEnum role) {
        this.user_id = user_id;
        this.password = password;
        this.username = username;
        this.introduction = introduction;
        this.role = role;
    }


}
