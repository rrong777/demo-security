package com.rrong777.web.async;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Callable;

@RestController
public class AsyncController {
    // getClass()是声明在Object中的方法，可以得到当前运行类的class对象。
    private Logger logger = LoggerFactory.getLogger(getClass());

    // 注入模拟队列
    @Autowired
    private MockQueue mockQueue;

    // 注入deferredResultHolder（deferredResult的容器
    // deferred 推迟
    @Autowired
    private DeferredResultHolder deferredResultHolder;
    // 下面的请求是一个标准的同步处理的方式
//    @RequestMapping("/order")
//    public String order() throws InterruptedException {
//        logger.info("主线程开始");
//        // 休眠1秒，假装在做一个下单的处理。
//        Thread.sleep(1000);
//        logger.info("主线程返回");
//        return "success";
//    }

    /**
     * 异步处理请求之Callable<String>
     *     （Runnable）这个泛型就是返回值类型
     * 像下面这样写，请求业务逻辑就放到副线程里面去处理了。
     * @return
     * @throws InterruptedException
     */
//    @RequestMapping("/order")
//    public Callable<String> order() throws InterruptedException {
//        logger.info("主线程开始");
//        // 这个Callable就是在Spring管理里面的线程，单开一个线程，去做业务逻辑的处理。
//        Callable<String> result = new Callable<String>() {
//            @Override
//            public String call() throws Exception {
//                logger.info("副线程开始");
//                // 休眠1秒，假装在做一个下单的处理。
//                Thread.sleep(1000);
//                logger.info("副线程返回");
//                return "success";
//            }
//        };
//
//        logger.info("主线程返回");
//        return result;
//    }
    @RequestMapping("/order")
    public DeferredResult<String> order() throws InterruptedException {
        logger.info("主线程开始");
        // 生成一个8位的随机数作为订单号
        String orderNumber = RandomStringUtils.randomNumeric(8);
        // 把订单号放到模拟队列里面去
        mockQueue.setPlaceHolder(orderNumber);
        // new一个deferredResult
        DeferredResult<String> result = new DeferredResult<>();
        // 把这个订单号作为key在map中存一个deferredResult
        deferredResultHolder.getMap().put(orderNumber, result);
        logger.info("主线程返回");
        return result;

        // 这个就是线程1  接收请求，把请求往消息队列里面塞
    }
}
