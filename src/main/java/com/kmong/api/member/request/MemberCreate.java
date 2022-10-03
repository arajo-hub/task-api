package com.kmong.api.member.request;

import com.kmong.api.config.encrypt.PwdEncryption;
import com.kmong.api.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberCreate {

    private String id;
    private String email;
    private String pwd;
    private String encryptedPwd;

    @Builder
    public MemberCreate(String id, String email, String pwd) {
        this.id = id;
        this.email = email;
        this.pwd = pwd;
        this.encryptedPwd = PwdEncryption.encrypt(pwd);
    }

    public Member toMember() {
        return Member.builder()
                    .id(id)
                    .email(email)
                    .pwd(encryptedPwd)
                    .build();
    }
}
