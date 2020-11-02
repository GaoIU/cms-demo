package com.gaoc.core.exception;

import cn.hutool.core.map.MapUtil;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 * @Description: 全局错误处理
 * @Date: 2020/5/15 14:56
 * @Author: Gaoc
 */
@Component
public class MyErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
        Integer status = MapUtil.getInt(errorAttributes, "status");

        if (status.equals(HttpStatus.GATEWAY_TIMEOUT.value())) {
            errorAttributes.put("message", "网络不佳，请稍后重试");
        } else if (status.equals(HttpStatus.TOO_MANY_REQUESTS.value())) {
            errorAttributes.put("message", "操作频繁，请稍后重试");
        } else if (status.equals(HttpStatus.NOT_FOUND.value())) {
            errorAttributes.put("message", "该资源不存在");
        } else if (status.equals(HttpStatus.SERVICE_UNAVAILABLE.value())) {
            errorAttributes.put("message", "服务器繁忙，请稍后重试");
        } else if (status.equals(HttpStatus.BAD_REQUEST.value())) {
            errorAttributes.put("message", "数据格式有误，请检查参数数据");
        }

        return errorAttributes;
    }

}
