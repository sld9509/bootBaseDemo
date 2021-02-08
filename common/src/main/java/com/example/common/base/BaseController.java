package com.example.common.base;

import com.example.common.dto.ResultData;
import com.example.common.enumeration.GlobalResultEnum;
import org.apache.commons.codec.Charsets;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 基础Controller
 * @author 孙灵达
 * @date 2021-02-08
 */
public class BaseController {

    public ResultData render(int code) {
        return new ResultData(code, null, null);
    }

    public ResultData render(int code, String message) {
        return new ResultData(code, message, null);
    }

    public ResultData render(int code, Object data) {
        return new ResultData(code, null, data);
    }

    public ResultData render(int code, String message, Object data) {
        return new ResultData(code, message, data);
    }

    public ResultData renderOK() {
        return new ResultData(GlobalResultEnum.SUCCESS.getCode(), GlobalResultEnum.SUCCESS.getMessage(), null);
    }

    public ResultData renderOK(String message) {
        return new ResultData(GlobalResultEnum.SUCCESS.getCode(), message, null);
    }

    public ResultData renderOKData(Object data) {
        return new ResultData(GlobalResultEnum.SUCCESS.getCode(), GlobalResultEnum.SUCCESS.getMessage(), data);
    }

    public ResultData renderFail() {
        return new ResultData(GlobalResultEnum.FAIL.getCode(), GlobalResultEnum.FAIL.getMessage(), null);
    }

    public ResultData renderFail(String message) {
        return new ResultData(GlobalResultEnum.FAIL.getCode(), message, null);
    }

    /**
     * 获取Request
     */
    public HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        // assert attributes != null;
        return attributes.getRequest();
    }

    /**
     * 获取Response
     */
    public HttpServletResponse getResponse() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        // assert attributes != null;
        return attributes.getResponse();
    }

    /**
     * request的流转成string
     */
    public String reqStreamToString() throws IOException {
        return StreamUtils.copyToString(getRequest().getInputStream(), Charset.forName(Charsets.UTF_8.name()));
    }
}
