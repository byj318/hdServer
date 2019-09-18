package com.huamai.hdServer.exception;

import com.huamai.hdServer.enums.ExceptionEnum;
import lombok.Getter;

/**
 * @author bystander
 * @date 2018/9/15
 *
 * 自定义异常类
 */
@Getter
public class MyException extends RuntimeException {

    private ExceptionEnum exceptionEnum;

    public MyException(ExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }


}
