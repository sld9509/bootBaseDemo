package com.example.user.controller;

import com.example.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 孙灵达
 * @date 2021-02-07
 */
@Slf4j
@RestController
@RequestMapping("test")
public class TestController {


    @RequestMapping("testMethod")
    public String testMethod() {
        boolean b = RedisUtil.setStr("bbb", "123456");
        return "OK";
    }
}
