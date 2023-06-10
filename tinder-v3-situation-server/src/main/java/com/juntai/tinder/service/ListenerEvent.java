package com.juntai.tinder.service;

import com.juntai.tinder.config.MetaConfig;
import com.juntai.tinder.event.MessageEvent;
import com.juntai.tinder.netty.handler.MyChannelHandler;
import com.juntai.tinder.task.ScheduledTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ConcurrentMap;

/**
 * @Description: $
 * @Author: nemo
 * @Date: 2022/9/19 2:46 PM
 */
@Slf4j
@Component
public class ListenerEvent implements ApplicationListener<MessageEvent> {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ScheduledTask scheduledTask;

    @Autowired
    private ScenarioEventService scenarioEventService;


    public void onApplicationEvent(MessageEvent event) {
        String msg = event.getMessage();
        //判断为最后一个channel 则关闭
        if(msg.startsWith("CLOSE_CHANNEL#")) {
            String simId = msg.replace("CLOSE_CHANNEL#", "");
            boolean contains = MyChannelHandler.simIdMap.values().contains(simId);
            if (!contains) {
                scheduledTask.deleteTask(simId);
                scenarioEventService.delete(simId);
            }
        }


    }
}

