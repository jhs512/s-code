package com.sbs.demo5.base.rsData;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
public class RsData<T> {
    private String resultCode;
    private String msg;
    private T data;

    public static <T> RsData<T> of(String resultCode, String msg, T data) {
        return new RsData<>(resultCode, msg, data);
    }

    public static <T> RsData<T> of(String resultCode, String msg) {
        return of(resultCode, msg, null);
    }

    public boolean isSuccess() {
        return resultCode.startsWith("S-");
    }

    public boolean isFail() {
        return !isSuccess();
    }

    public Optional<RsData<T>> optional() {
        return Optional.of(this);
    }
}
