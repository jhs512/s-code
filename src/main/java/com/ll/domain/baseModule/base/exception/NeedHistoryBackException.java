package com.ll.domain.baseModule.base.exception;

import com.ll.global.rsData.RsData;

public class NeedHistoryBackException extends RuntimeException {
    public NeedHistoryBackException(RsData rs) {
        this(rs.getMsg());
    }

    public NeedHistoryBackException(String msg) {
        super(msg);
    }
}
