package com.example.common.exception;

import com.example.common.enumeration.GlobalResultEnum;
import lombok.Getter;

/**
 * 全局自定义异常
 * @author 孙灵达
 * @date 2021-02-08
 */
@Getter
public class GlobalException extends RuntimeException {

    /**
     * 返回码
     */
    private int code = GlobalResultEnum.FAIL.getCode();

    /**
     * 返回描述
     */
    private String message;

    public GlobalException(GlobalResultEnum result) {
        super(result.getMessage());
        this.code = result.getCode();
        this.message = result.getMessage();
    }


    public GlobalException(int code, String errMsg) {
        super(errMsg);
        this.code = code;
        this.message = errMsg;
    }

    public GlobalException(String errMsg, Exception e) {
        super(errMsg, e);
    }
}
