package com.rrong777.web.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

// 模拟消息队列的对象
@Component
public class MockQueue {
    private Logger logger = LoggerFactory.getLogger(getClass());
    // 下单消息，这个对象有值的时候就认为接到一个下单消息
    private String placeHolder;
    // 订单完成消息 这个对象有值的时候就认为接到一个订单完成消息
    private String completeOrder;

    public String getPlaceHolder() {
        return placeHolder;
    }

    public void setPlaceHolder(String placeHolder) throws InterruptedException {
        // 开启一个匿名线程
        // 这个过程是模拟消费者异步从队列消费消息
        new Thread(() -> {
            logger.info("接到下单请求！" + placeHolder);
            // 这里休眠一秒，就假装这一秒钟消息队列收到下单消息，然后消费者来消费这个下单请求消息
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 上面休眠假设在处理订单，处理完之后这里给订单完成消息设置值，设置值之后就可以返回了
            this.completeOrder = placeHolder;
            logger.info("下单请求处理完毕！" + placeHolder);
        }).start();
    }

    public String getCompleteOrder() {
        return completeOrder;
    }

    public void setCompleteOrder(String completeOrder) {
        this.completeOrder = completeOrder;
    }
}
