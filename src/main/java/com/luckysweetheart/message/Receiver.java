package com.luckysweetheart.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;

/**
 * 这个Receiver类将会被注册为一个消息监听者时。处理消息的方法我们可以任意命名，我们有相当大的灵活性。
 * <p>
 * 我们给Receiver的构造函数通过@AutoWired标注注入了一个CountDownLatch实例，当接收到消息时，调用cutDown()方法。
 * </p>
 * Created by yangxin on 2017/6/21.
 */
public class Receiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

    private CountDownLatch latch;

    @Autowired
    public Receiver(CountDownLatch latch) {
        this.latch = latch;
    }

    public void receiveMessage(String message) {
        LOGGER.info("Received <" + message + ">");
        latch.countDown();
    }
}
