package com.ll.domain.baseModule.base.exception;

import com.ll.global.rsData.RsData;

public class GlobalException extends RuntimeException {
    private final RsData rs;

    public GlobalException(String resultCode, String msg) {
        this(RsData.of(resultCode, msg));
    }

    public GlobalException(RsData rs) {
        super(rs.getMsg());
        this.rs = rs;
    }
}
