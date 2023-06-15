package com.juntai.tinder.netty.handler;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/3/22
 */
@Component
public class MyChannelHandler {
    /**
     * 用于存放用户Channel信息，也可以建立map结构模拟不同的消息群
     */
    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static ConcurrentMap<String, String> simIdMap = new ConcurrentHashMap<>(16);


}
