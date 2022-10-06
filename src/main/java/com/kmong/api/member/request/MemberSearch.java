package com.kmong.api.member.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class MemberSearch {

    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9]*$", message = "아이디는 첫시작을 영어로, 구성은 영숫자로만 가능합니다.")
    @NotBlank(message = "아이디는 필수항목입니다.")
    @Size(min = 1, max = 15, message = "아이디는 1자부터 15자까지 가능합니다.")
    private String memberId;

    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자 조합으로만 가능합니다.")
    @NotBlank(message = "비밀번호는 필수항목입니다.")
    private String pwd;

    @Builder
    public MemberSearch(String memberId, String pwd) {
        this.memberId = memberId;
        this.pwd = pwd;
    }
}
