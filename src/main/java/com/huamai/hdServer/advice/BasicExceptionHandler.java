package com.huamai.hdServer.advice;

import com.huamai.hdServer.exception.MyException;
import com.huamai.hdServer.vo.ExceptionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author bystander
 * @date 2018/9/15
 *
 * 自定义异常处理
 */
@Slf4j
@RestControllerAdvice
public class BasicExceptionHandler {

    @ExceptionHandler(MyException.class)
    public ExceptionResult handleException(MyException e) {
        return new ExceptionResult(e.getExceptionEnum());
    }
}
