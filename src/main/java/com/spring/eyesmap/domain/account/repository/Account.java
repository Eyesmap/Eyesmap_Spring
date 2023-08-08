package com.spring.eyesmap.domain.account.repository;

import com.spring.eyesmap.global.enumeration.Role;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class Account {

    @Id
    private String id;

    @Column
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Account(String id, String nickname, Role role){
        this.id = id;
        this.nickname = nickname;
        this.role = role;
    }
}
