package com.kmong.api.member.domain;

import com.kmong.api.member.response.MemberView;
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

    @Builder
    public Member(String id, String email, String pwd) {
        this.id = id;
        this.email = email;
        this.pwd = pwd;
    }

    public MemberView toMemberView() {
        return MemberView.builder()
                            .id(id)
                            .email(email)
                            .build();
    }
}
