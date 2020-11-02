package com.gaoc.security.service;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: 身份未认证
 * @Date: 2020/7/29 17:39
 * @Author: Gaoc
 */
@Slf4j
//@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String AJAX_HEADER = "X-Requested-With";

    private final String AJAX_VALUE = "XMLHttpRequest";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        boolean isAjax = false;
        String header = request.getHeader(AJAX_HEADER);
        if (StrUtil.isNotBlank(header) && StrUtil.equals(header, AJAX_VALUE)) {
            isAjax = true;
        }
        log.info("=================请求未进行身份认证，请求是否为ajax请求：{}=======================", isAjax);

        if (isAjax) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        } else {
            response.sendRedirect("/");
        }
    }

}
