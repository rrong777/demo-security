package com.rrong777.web.async;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;

/**
 * 在接收下单请求并塞入消息队列的线程和监听消息队列返回结果的线程之间
 * 传递deferredResult（DeferredResultHolder）
 */
@Component
public class DeferredResultHolder {
    // key为订单号，每个订单号对应一个订单处理结果。
    private Map<String, DeferredResult<String>> map = new HashMap<>();

    public Map<String, DeferredResult<String>> getMap() {
        return map;
    }

    public void setMap(Map<String, DeferredResult<String>> map) {
        this.map = map;
    }
}
