package com.nowcoder.community.controller.advice;

import com.nowcoder.community.util.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


//不用处理任何Controller 就可以处理所有的controller组件

//限制范围  只会扫描带有controller注解的bean
@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler({Exception.class})
    public void handleException(Exception e, HttpServletRequest request,
                                HttpServletResponse response) throws IOException {
        logger.error("服务器发生异常" + e.getMessage());
        //详细的遍历异常信息
        for (StackTraceElement element : e.getStackTrace()) {
            logger.error(element.toString());
        }
        //判断是普通请求还是json
        //通过request来判断
        String xRequestedWith = request.getHeader("x-requested-with");
        //说明返回的是html 异步请求
        if ("XMLHttpRequest".equals(xRequestedWith)) {
            //响应一个字符串
            //像浏览器返回的是普通的字符串，手动将它转换为json字符串
            response.setContentType("application/plain;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(CommunityUtil.getJSONString(1, "服务器异常"));
        } else {
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }
}
