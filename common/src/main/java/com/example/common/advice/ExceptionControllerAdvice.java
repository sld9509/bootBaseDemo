package com.example.common.advice;

import cn.hutool.core.exceptions.ValidateException;
import com.example.common.constant.GlobalConstant;
import com.example.common.dto.ResultData;
import com.example.common.enumeration.GlobalResultEnum;
import com.example.common.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * @author 孙灵达
 * @date 2021-02-08
 */
@Slf4j
@ControllerAdvice
public class ExceptionControllerAdvice {

    /**
     * Exception异常
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResultData exceptionHandler(Exception e) {
        log.error(e.getMessage(), e);
        ResultData resultData = new ResultData();
        resultData.setCode(500);
        resultData.setMessage("未知错误");
        return resultData;
    }

    /**
     * GlobalException自定义全局异常
     */
    @ResponseBody
    @ExceptionHandler(value = GlobalException.class)
    public ResultData globalExceptionHandler(GlobalException e) {
        log.error(e.getMessage(), e);
        ResultData resultData = new ResultData();
        resultData.setCode(500);
        resultData.setMessage(e.getMessage());
        return resultData;
    }

    /**
     * 参数校验异常
     */
    @ResponseBody
    @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class, ConstraintViolationException.class, ValidateException.class})
    public ResultData resolveParamException(Exception exception) {
        if (exception instanceof BindException) {
            String error = ((BindException) exception)
                    .getBindingResult()
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(GlobalConstant.SEMICOLON));
            return getResult(GlobalResultEnum.FAIL.getCode(), error);
        } else if (exception instanceof ConstraintViolationException) {
            String error = ((ConstraintViolationException) exception)
                    .getConstraintViolations()
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(GlobalConstant.SEMICOLON));
            return getResult(GlobalResultEnum.FAIL.getCode(), error);
        } else if (exception instanceof  ValidateException) {
            return getResult(GlobalResultEnum.FAIL.getCode(), exception.getMessage());
        }
        return getResult(GlobalResultEnum.FAIL.getCode(), GlobalResultEnum.FAIL.getMessage());
    }

    private ResultData getResult(int code, String message) {
        return new ResultData(code, message, null);
    }
}
