package com.spring.eyesmap.domain.account.domain;

import com.spring.eyesmap.global.enumeration.Role;
import com.spring.eyesmap.global.enumeration.VoiceOnOff;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity(name = "account")
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

    @Column
    private String profileImageUrl;

    @Column
    private String imageName;

    @Column
    private VoiceOnOff voiceOnOff;

    @Builder
    public Account(Long userId, String nickname, Role role, String profileImageUrl, String imageName, VoiceOnOff voiceOnOff){
        this.userId = userId;
        this.nickname = nickname;
        this.role = role;
        this.profileImageUrl = profileImageUrl;
        this.imageName = imageName;
        this.voiceOnOff = voiceOnOff;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateVoiceOnOff(VoiceOnOff voiceOnOff){
        this.voiceOnOff = voiceOnOff;
    }

    public void updateImage(String profileImageUrl, String imageName) {
        this.profileImageUrl = profileImageUrl;
        this.imageName = imageName;
    }
}
