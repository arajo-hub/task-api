package com.kmong.api.member.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id
    private String id;
    @Column
    private String email;
    @Column
    private String pwd;
    @Column
    private String sessionId;

    @Builder
    public Member(String id, String email, String pwd, String sessionId) {
        this.id = id;
        this.email = email;
        this.pwd = pwd;
        this.sessionId = sessionId;
    }
}
