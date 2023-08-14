package com.spring.eyesmap.domain.account.repository;

import com.spring.eyesmap.global.enumeration.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Account {

    @Id
    private Long id;

    @Column
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Account(Long id, String nickname, Role role){
        this.id = id;
        this.nickname = nickname;
        this.role = role;
    }
}
