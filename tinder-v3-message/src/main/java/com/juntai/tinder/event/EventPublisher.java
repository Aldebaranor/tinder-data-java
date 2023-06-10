package com.juntai.tinder.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * @Description: $
 * @Author: nemo
 * @Date: 2022/9/19 2:54 PM
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class EventPublisher {
    /**
     * 注入ApplicationContext用来发布事件
     */
    private final ApplicationContext applicationContext;

    public String publish(String msg) {
        applicationContext.publishEvent(new MessageEvent(this, msg));
        return "OK";
    }
}
