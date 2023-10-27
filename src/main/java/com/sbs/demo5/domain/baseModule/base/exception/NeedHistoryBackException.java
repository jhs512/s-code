package com.sbs.demo5.domain.baseModule.base.exception;

import com.sbs.demo5.global.rsData.RsData;

public class NeedHistoryBackException extends RuntimeException {
    public NeedHistoryBackException(RsData rs) {
        this(rs.getMsg());
    }

    public NeedHistoryBackException(String msg) {
        super(msg);
    }
}
