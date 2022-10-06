package com.kmong.api.member.request;

import com.kmong.api.config.encrypt.PwdEncryption;
import com.kmong.api.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class MemberCreate {

    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9]*$", message = "아이디는 첫시작을 영어로, 구성은 영숫자로만 가능합니다.")
    @NotBlank(message = "아이디는 필수항목입니다.")
    @Size(min = 1, max = 15, message = "아이디는 1자부터 15자까지 가능합니다.")
    private String memberId;

    @NotNull(message = "이메일은 필수항목입니다.")
    @Size(min = 1, message = "이메일을 입력해주세요.")
    @Email(message = "잘못된 이메일 형식입니다.")
    private String email;

    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자 조합으로만 가능합니다.")
    @NotBlank(message = "비밀번호는 필수항목입니다.")
    @Size(min = 1, max = 15, message = "비밀번호는 1자부터 15자까지 가능합니다.")
    private String pwd;

    @Builder
    public MemberCreate(String memberId, String email, String pwd) {
        this.memberId = memberId;
        this.email = email;
        this.pwd = pwd;
    }

    public Member toMember() {
        return Member.builder()
                    .id(memberId)
                    .email(email)
                    .pwd(PwdEncryption.encrypt(pwd))
                    .build();
    }
}
