package com.kmong.api.member.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class MemberSearch {

    @NotNull(message = "아이디는 필수항목입니다.")
    @Size(min = 1, max = 15, message = "아이디는 1자부터 15자까지 가능합니다.")
    private String memberId;

    @NotNull(message = "비밀번호는 필수항목입니다.")
    @Size(min = 1, max = 15, message = "비밀번호는 1자부터 15자까지 가능합니다.")
    private String pwd;

    @Builder
    public MemberSearch(String memberId, String pwd) {
        this.memberId = memberId;
        this.pwd = pwd;
    }
}
