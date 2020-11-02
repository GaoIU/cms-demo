package com.gaoc.security.service;

import com.alibaba.fastjson.JSON;
import com.gaoc.core.model.R;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: 权限认证失败
 * @Date: 2020/7/29 17:34
 * @Author: Gaoc
 */
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String s = JSON.toJSONString(R.fail(HttpStatus.FORBIDDEN.value(), "权限不足"));
        response.getWriter().write(s);
        response.getWriter().flush();
        response.getWriter().close();
    }

}
