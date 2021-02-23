package com.rrong777.web.async;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

// 监听模拟消息队列 虚拟的消息队列中 completeOrder这个字段被设置上值的时候，这时候这个线程就认为是下单业务处理完毕，这时候就可以
// 把对应的结果返回给前端。


// ApplicationListener接口 要去了解
@Component
public class QueueListener implements ApplicationListener<ContextRefreshedEvent> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private MockQueue mockQueue;
    @Autowired
    private DeferredResultHolder deferredResultHolder;

    // ContextRefreshedEvent这个事件是整个容器初始化完毕的一个事件
    // 监听这个事件就相当于整个系统我要启动起来要做的一个事情。
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        // 当前死循环是在应用启动过程中就开始执行的。 无限的死循环，如果没有新开线程的话。就会把整个程序阻塞掉。所以这里要用多线程
        new Thread(() -> {
            // 系统启动起来之后，我要做的事情就是监听模拟队列里面的completeHolder里面的值
            while(true) {
                // 无限的一个循环，模拟的队列里面的 complteOrder不为空 有值了。
                if(StringUtils.isNotBlank(mockQueue.getCompleteOrder())) {
                    String orderNumber = mockQueue.getCompleteOrder();
                    logger.info(" 返回订单处理结果：" + orderNumber);
                    // 当从这个deferredResultHolder容器拿到一个deferredResult，并且setResult()之后。 就意味着我整个异步
                    // 处理完成了。要往浏览器返回结果了。 这个setResult就是你最后要返回的信息。
                    deferredResultHolder.getMap().get(orderNumber).setResult("place order success");
                    mockQueue.setCompleteOrder(null); // 把订单完成消息设置成空。就不会反复的循环去处理这段代码。
                    // 如果没有值，证明没有订单处理完毕
                } else {
                    // 没值的时候休眠100毫秒。
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
