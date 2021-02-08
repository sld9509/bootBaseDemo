package com.example.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 孙灵达
 * @date 2021-02-08
 */
@Getter
@AllArgsConstructor
public enum GlobalResultEnum {

    /**
     * 成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 失败
     */
    FAIL(500, "未知错误"),

    ;

    private int code;

    private String message;
}
