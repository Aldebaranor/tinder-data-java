package com.juntai.tinder.zeromq;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
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
 * @Description: 生产者$
 * @Author: nemo
 * @Date: 2022/6/25 2:33 PM
 */
@Configuration
@ConditionalOnProperty(value = "zmq.producer.enabled", havingValue = "true")
@ConfigurationProperties(prefix = "zmq.producer")
@Slf4j
@Data
public class ZeroMqPublisher {

//    private String serverKey;

    private String address;

    private String topic;
    private ZContext zContext;

    private ZAuth zAuth;

    private ZMQ.Socket zmqSocket;

    public void publish(String s) {
        zmqSocket.send(String.format("%s:%s", topic, s));
    }

    public void publish(String topic, String s) {
        zmqSocket.send(String.format("%s:%s", topic, s));
    }

    @PostConstruct
    public void start() {
        zContext = new ZContext();
        //zAuth = new ZAuth(zContext);
        //zAuth.configureCurve(ZAuth.CURVE_ALLOW_ANY);

        zmqSocket = zContext.createSocket(SocketType.PUB);
//        ZCert zCert = new ZCert();
//        zmqSocket.setCurvePublicKey(zCert.getPublicKey());
//        zmqSocket.setCurveSecretKey(zCert.getSecretKey());
//        zmqSocket.setCurveServerKey(Z85.decode(serverKey));
        zmqSocket.bind(address);
        log.info("ZeroMqPublisher start success：" + address);
    }

    @PreDestroy
    public void destory() throws IOException {
        zAuth.close();
        zContext.close();
        log.info("----------------------------关闭ZeroMqPublisher");
    }
}
