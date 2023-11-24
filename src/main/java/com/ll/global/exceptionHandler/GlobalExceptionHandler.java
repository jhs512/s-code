package com.ll.global.exceptionHandler;

import com.ll.domain.baseModule.base.exception.HttpException;
import com.ll.domain.baseModule.base.exception.NeedHistoryBackException;
import com.ll.global.rq.Rq;
import com.ll.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<RsData<?>> handle(HttpException ex) {
        return new ResponseEntity<>(RsData.of(ex.getResultCode(), ex.getMsg()), HttpStatus.valueOf(ex.getStatusCode()));
    }
}
