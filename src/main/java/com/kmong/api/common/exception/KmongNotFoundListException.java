package com.kmong.api.common.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KmongNotFoundListException extends KmongNotFoundException {

    private List list;

    public KmongNotFoundListException(String message, List list) {
        super(message);
        this.list = list;
    }

}
