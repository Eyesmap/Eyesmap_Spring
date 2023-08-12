package com.spring.eyesmap.domain.account.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity(name = "user")
@RequiredArgsConstructor
@Getter
public class Account {
    @Id
    private Long id;

    private boolean reporting;
    private String phoneNum;

}
