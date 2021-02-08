package com.example.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 孙灵达
 * @date 2020-12-30
 */
@Getter
@AllArgsConstructor
public enum DeleteEnum {

    /**
     * 已删除
     */
    YES(1),

    /**
     * 未删除
     */
    NO(0),

    ;

    private final int code;
}
