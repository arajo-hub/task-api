package com.kmong.api.member.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberSearch {

    private String id;
    private String email;
    private String pwd;

    @Builder
    public MemberSearch(String id, String email, String pwd) {
        this.id = id;
        this.email = email;
        this.pwd = pwd;
    }
}
