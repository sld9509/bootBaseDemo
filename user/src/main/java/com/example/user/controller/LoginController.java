package com.example.user.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

/**
 * @author 孙灵达
 * @date 2021-02-07
 */
@Validated
@RestController
@RequestMapping("login")
public class LoginController {

    /**
     * 登录
     * @param loginName     登录账号
     * @param passWord      登录密码
     */
    @RequestMapping("login")
    public String login(@NotBlank(message = "登录账号不能为空") String loginName,
                        @NotBlank(message = "登录密码不能为空") String passWord) {
        return "OK";
    }
}
