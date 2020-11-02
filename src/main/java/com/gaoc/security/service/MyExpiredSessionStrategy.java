package com.gaoc.security.service;

import cn.hutool.http.HttpStatus;
import com.gaoc.core.model.R;
import org.springframework.http.MediaType;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: session并发登录
 * @Date: 2020/7/3 0:24
 * @Author: Gaoc
 */
@Service
public class MyExpiredSessionStrategy implements SessionInformationExpiredStrategy {

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent sessionInformationExpiredEvent) throws IOException, ServletException {
        HttpServletResponse response = sessionInformationExpiredEvent.getResponse();
        R.printWriter(R.fail(HttpStatus.HTTP_UNAUTHORIZED, "已在别处登录"), response, MediaType.APPLICATION_JSON_VALUE);
    }

}
