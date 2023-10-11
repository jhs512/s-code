package com.sbs.demo5.base.exceptionHandler;

import com.sbs.demo5.base.rq.Rq;
import com.sbs.demo5.domain.base.exception.NeedHistoryBackException;
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
