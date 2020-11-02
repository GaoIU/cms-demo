package com.gaoc.core.config;

import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Description: 自定义错误页面
 * @Date: 2020/7/27 12:20
 * @Author: Gaoc
 */
@Component
public class ErrorPageResovler implements ErrorViewResolver {

    @Override
    public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
        ModelAndView mav = new ModelAndView();

        if (status == HttpStatus.BAD_REQUEST || status == HttpStatus.NOT_FOUND || status == HttpStatus.METHOD_NOT_ALLOWED) {
            mav.setViewName("error/404");
        } else if (status == HttpStatus.FORBIDDEN) {
            mav.setViewName("error/403");
        } else {
            mav.setViewName("error/500");
        }

        return mav;
    }

}
