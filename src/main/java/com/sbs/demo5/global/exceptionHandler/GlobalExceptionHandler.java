package com.sbs.demo5.global.exceptionHandler;

import com.sbs.demo5.domain.baseModule.base.exception.NeedHistoryBackException;
import com.sbs.demo5.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final Rq rq;

    @ExceptionHandler(NeedHistoryBackException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handle(NeedHistoryBackException ex) {
        return rq.historyBack(ex.getMessage());
    }
}
