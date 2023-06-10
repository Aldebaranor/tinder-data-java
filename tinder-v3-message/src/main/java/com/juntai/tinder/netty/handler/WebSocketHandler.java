package com.juntai.tinder.netty.handler;

import com.juntai.tinder.config.ApplicationContextProvider;
import com.juntai.tinder.event.EventPublisher;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Description: $
 * @Author: nemo
 * @Date: 2023/4/14 23:20
 */
@Component
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("有websocket客户端建立连接，通道开启！" + ctx.channel().toString());
        //添加到channelGroup通道组
        MyChannelHandler.channelGroup.add(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("websocket客户端断开连接，通道关闭！" + ctx.channel().toString());
        //从channelGroup通道组删除
        MyChannelHandler.channelGroup.remove(ctx.channel());
        String simId = MyChannelHandler.simIdMap.get(ctx.channel().id());
        MyChannelHandler.simIdMap.remove(ctx.channel().id());
        //触发仿真运行线程关闭的逻辑
        EventPublisher eventPublisher = ApplicationContextProvider.getBean(EventPublisher.class);
        eventPublisher.publish("CLOSE_CHANNEL#"+ simId);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //接收的消息
        System.out.println(String.format("收到客户端%s的数据：%s", ctx.channel().id(), msg.text()));
    }

    public void sendMessage(ChannelHandlerContext ctx, String msg) throws InterruptedException {
        for (Channel channel : MyChannelHandler.channelGroup) {
            if (!channel.id().asLongText().equals(ctx.channel().id().asLongText())) {
                channel.writeAndFlush(new TextWebSocketFrame(msg));
            }
        }
    }

    public void sendAllMessage(String msg) {
        for (Channel channel : MyChannelHandler.channelGroup) {
            channel.writeAndFlush(new TextWebSocketFrame(msg));
        }
    }

    public void sendAllMessage(String simId,String topic,String msg) {
        for (Channel channel : MyChannelHandler.channelGroup) {
//            String id = MyChannelHandler.simIdMap.get(channel.id());
//            if(StringUtils.isEmpty(id)){
//                continue;
//            }
//
//            if(!StringUtils.equals(id,simId)){
//                continue;
//            }
            channel.writeAndFlush(new TextWebSocketFrame(String.format("%s$%s$%s",simId,topic,msg)));
        }
    }




}
