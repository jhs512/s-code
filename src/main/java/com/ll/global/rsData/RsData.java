package com.ll.global.rsData;

import lombok.Getter;

import java.util.Optional;

@Getter
public class RsData<T> {
    private final String resultCode;
    private final String msg;
    private final T data;
    private final int statusCode;

    public RsData(String resultCode, String msg, T data) {
        this.resultCode = resultCode;
        this.msg = msg;
        this.data = data;

        String[] resultCodeBits = resultCode.split("-", 3);

        if (resultCodeBits.length == 3) {
            this.statusCode = Integer.parseInt(resultCodeBits[1]);
        } else if (resultCodeBits.length == 2 && resultCodeBits[1].matches("^-?\\d+$")) {
            int middle = Integer.parseInt(resultCodeBits[1]);

            if (middle >= 100) {
                this.statusCode = middle;
            } else {
                this.statusCode = resultCode.startsWith("S-") ? 200 : 400;
            }
        } else {
            this.statusCode = resultCode.startsWith("S-") ? 200 : 400;
        }
    }

    public static <T> RsData<T> of(String resultCode, String msg, T data) {
        return new RsData<>(resultCode, msg, data);
    }

    public static <T> RsData<T> of(String resultCode, String msg) {
        return of(resultCode, msg, null);
    }

    public boolean isSuccess() {
        return statusCode >= 200 && statusCode < 300;
    }

    public boolean isFail() {
        return !isSuccess();
    }

    public Optional<RsData<T>> optional() {
        return Optional.of(this);
    }

    public <T> RsData<T> newDataOf(T data) {
        return new RsData<T>(getResultCode(), getMsg(), data);
    }
}
