package com.rrong777.web.session;

import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import java.io.IOException;

public class Rrrong777ExpiredSessionStrategy implements SessionInformationExpiredStrategy {
    /**
     *
     * @param eventØ session超时事件 这个事件封装了导致了踢用户这个行为产生的请求和响应，从这个请求里你就可以获得你需要记录的一些信息
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent eventØ) throws IOException, ServletException {
        // 没设置响应的contenttype  会变成乱码
        eventØ.getResponse().setContentType("application/json;charset=UTF-8");
        eventØ.getResponse().getWriter().write("并发登录");
    }
}
