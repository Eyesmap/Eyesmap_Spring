package com.spring.eyesmap.domain.account.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;

@Entity
public class Account {

    @Id
    private Long id;

    @Column
    private String nickname;

    @Builder
    public Account(Long id, String nickname){
        this.id = id;
        this.nickname = nickname;
    }
}
