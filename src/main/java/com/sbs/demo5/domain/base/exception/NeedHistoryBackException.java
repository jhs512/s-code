package com.sbs.demo5.domain.base.exception;

import com.sbs.demo5.base.rsData.RsData;

public class NeedHistoryBackException extends RuntimeException {
    public NeedHistoryBackException(RsData rs) {
        this(rs.getMsg());
    }

    public NeedHistoryBackException(String msg) {
        super(msg);
    }
}
