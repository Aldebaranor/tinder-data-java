package com.juntai.tinder.zeromq;

import com.juntai.tinder.zeromq.service.SubscriptionListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.zeromq.SocketType;
import org.zeromq.ZAuth;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * @Description: 消费者$
 * @Author: nemo
 * @Date: 2022/6/25 2:48 PM
 */
@Configuration
@ConditionalOnProperty(value = "zmq.subscribe.enabled", havingValue = "true")
@ConfigurationProperties(prefix = "zmq.subscribe")
@Data
@Slf4j
public class ZeroMqSubscriber {

//    private String publicKey;
//
//    private String secretKey;

    private String address;

    private String topic;
    private ZContext zContext;

    private ZAuth zAuth;

    private ZMQ.Socket zmqSocket;

    @Autowired
    private SubscriptionListener subscriptionListener;

    @PostConstruct
    public void subscribe() {
        init();
        if (StringUtils.isBlank(topic)) {
            zmqSocket.subscribe(ZMQ.SUBSCRIPTION_ALL);
        }
        zmqSocket.subscribe(topic);

    }

    public void unsubscribe() {
        zmqSocket.unsubscribe(ZMQ.SUBSCRIPTION_ALL);
    }

    public void init() {
        zContext = new ZContext();

        // zAuth = new ZAuth(zContext);
        // zAuth.configureCurve(ZAuth.CURVE_ALLOW_ANY);

        zmqSocket = zContext.createSocket(SocketType.SUB);
//       zmqSocket.setCurveServer(true);
//        zmqSocket.setCurvePublicKey(Z85.decode(publicKey));
//        zmqSocket.setCurveSecretKey(Z85.decode(secretKey));
        zmqSocket.connect(address);
        zmqSocket.setHeartbeatIvl(5 * 60 * 1000);
        zmqSocket.setHeartbeatTimeout(60 * 1000);
        zmqSocket.setHeartbeatTtl(10 * 60 * 1000);
        Thread zeroMqSubscriberThread = new ZeroMqSubscriberThread();
        zeroMqSubscriberThread.setName(zeroMqSubscriberThread.getClass().getSimpleName());
        zeroMqSubscriberThread.start();
        log.info("ZeroMqSubscribe start success ："+address);
    }

    @PreDestroy
    public void destory() throws IOException {
        unsubscribe();
        zAuth.close();
        zContext.close();
        log.info("----------------------------关闭ZeroMqSubscribe");
    }

    private final class ZeroMqSubscriberThread extends Thread {

        private final Logger logger = LoggerFactory.getLogger(ZeroMqSubscriberThread.class);

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    String b1 = zmqSocket.recvStr();
                    byte[] b2 = zmqSocket.recv();
                    try {
                        if (subscriptionListener != null) {
                            subscriptionListener.onReceiveDouble(b1, b2);
                        }
                    } catch (Exception e) {

                        logger.error(ExceptionUtils.getRootCauseMessage(e), e);
                    }
                }
            } catch (Exception e) {
                logger.error(ExceptionUtils.getRootCauseMessage(e), e);
            }
        }

    }

}

