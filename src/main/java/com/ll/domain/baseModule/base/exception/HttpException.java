package com.ll.domain.baseModule.base.exception;

import lombok.Getter;

@Getter
public class HttpException extends RuntimeException {
    private int statusCode;
    private String resultCode;
    private String msg;

    public HttpException(int statusCode, String resultCode, String msg) {
        super(msg);
        this.statusCode = statusCode;
        this.resultCode = resultCode;
        this.msg = msg;
    }
}