package com.example.common.advice;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.example.common.constant.GlobalConstant;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 孙灵达
 * @date 2021-02-08
 */
@Slf4j
@Aspect
@Order(0)
@Component
public class LogOutputAdvice {

    /**
     * controller层
     */
    private static final String CONTROLLER = "execution(* com.example..*Controller.*(..))";

    /**
     * 对包下所有的controller结尾的类的所有方法增强
     */
    @Around(CONTROLLER)
    public Object processLog(ProceedingJoinPoint pjp) throws Throwable {
        String logSequence = "log-seq-" + IdWorker.getIdStr();
        // 打印请求参数参数
        long start = System.currentTimeMillis();
        requestLoging(logSequence, pjp);
        Object result = null;
        try {
            // 执行目标方法
            result = pjp.proceed();
        } finally {
            // 获取执行完的时间 打印返回报文
            responseLog(logSequence, start, result);
        }
        return result;
    }

    /**
     * 打印请求日志
     */
    private static void requestLoging(String logSequence, ProceedingJoinPoint pjp) {
        // 获取参数名称
        String[] parameterNamesArgs = ((MethodSignature) pjp.getSignature()).getParameterNames();
        // 获取方法参数
        Object[] args = pjp.getArgs();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        log.info("==========================================================================");
        log.info("REQUEST         :" + logSequence);
        log.info("请求方式         :" + request.getMethod());
        log.info("请求Controller   :" + pjp.getSignature().getDeclaringTypeName());
        log.info("请求接口         :" + request.getRequestURI());

        StringBuilder paramsBuf = new StringBuilder();
        // 获取请求参数集合并进行遍历拼接
        for (int i = 0; i < args.length; i++) {
            if (paramsBuf.length() > 0) {
                paramsBuf.append(" ");
            }
            paramsBuf.append(parameterNamesArgs[i]).append("=").append(args[i]);
        }
        if (paramsBuf.length() > 0) {
            log.info("请求参数       :" + paramsBuf.toString());
        }
    }

    /**
     * 打印输出日志
     */
    private static void responseLog(String logSequence, long start, Object obj) {
        log.info("RESPONSE        :" + logSequence);
        log.info("处理时间(毫秒)   :" + (System.currentTimeMillis() - start));
        String result = JSON.toJSONString(obj);
        if (result.length() > GlobalConstant.LOG_MAX_LENGTH) {
            result = result.substring(0, GlobalConstant.LOG_MAX_LENGTH);
        }
        log.info("返回结果          :" + result);
        log.info("==========================================================================");
    }
}
