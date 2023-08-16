package com.spring.eyesmap.domain.account.domain;

import com.spring.eyesmap.global.enumeration.Role;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Account {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Role role;


    @Builder
    public Account(Long userId, String nickname, Role role){
        this.userId = userId;
        this.nickname = nickname;
        this.role = role;
    }
}
