package com.example.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 孙灵达
 * @date 2020-12-30
 */
@Getter
@AllArgsConstructor
public enum StatusEnum {

    /**
     * 有效
     */
    VALID(1),

    /**
     * 无效
     */
    INVALID(0 ),

    ;

    private final int code;
}
