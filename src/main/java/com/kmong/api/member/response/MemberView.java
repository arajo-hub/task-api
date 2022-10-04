package com.kmong.api.member.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberView {

    private String id;
    private String email;

    @Builder
    public MemberView(String id, String email) {
        this.id = id;
        this.email = email;
    }
}
