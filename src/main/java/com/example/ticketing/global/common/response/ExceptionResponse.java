package com.example.ticketing.global.common.response;

import lombok.Getter;

@Getter
public class ExceptionResponse extends CommonResponse {

    public ExceptionResponse(String message, int code) {
        super(false, code, message);
    }
}