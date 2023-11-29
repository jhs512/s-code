package com.ll.global.exceptionHandler;

import com.ll.domain.baseModule.base.exception.GlobalException;
import com.ll.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final Rq rq;

    @ExceptionHandler(GlobalException.class)
    public String handle(GlobalException ex) {
        return rq.historyBack(ex.getMessage());
    }
}
