package com.juntai.tinder.netty;


import com.juntai.tinder.netty.handler.MyChannelHandler;
import com.juntai.tinder.netty.handler.WebSocketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @Description: websocket$
 * @Author: nemo
 * @Date: 2023/4/14 22:54
 */
@Data
@Slf4j
@Service
@ConfigurationProperties(prefix = "netty")
public class NettyWebSocketServer implements Runnable {

    private Integer serverPort;

    private EventLoopGroup bossGroup;
    private EventLoopGroup group;

    private Channel channel;

    private Thread thread;

    @PostConstruct
    public void init() {
        thread = new Thread(this);
        thread.start();
    }

    @PreDestroy
    public void stop() {

            System.out.println("websocket已启动");
            group.shutdownGracefully();
            bossGroup.shutdownGracefully();
            channel.close();


    }


    @SneakyThrows
    @Override
    public void run() {

        ServerBootstrap sb = new ServerBootstrap();
        bossGroup = new NioEventLoopGroup();
        group = new NioEventLoopGroup();
        try {
            sb.option(ChannelOption.SO_BACKLOG, 1024);
            sb.group(group, bossGroup) // 绑定线程池
                    .channel(NioServerSocketChannel.class) // 指定使用的channel
                    .localAddress(this.serverPort)// 绑定监听端口
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 绑定客户端连接时候触发操作

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //websocket协议本身是基于http协议的，所以这边也要使用http解编码器
                            ch.pipeline().addLast(new HttpServerCodec());
                            //以块的方式来写的处理器
                            ch.pipeline().addLast(new ChunkedWriteHandler());
                            ch.pipeline().addLast(new HttpObjectAggregator(8192));
                            ch.pipeline().addLast(new WebSocketServerProtocolHandler("/tinder-websocket", null, true, 65536 * 10));
                            ch.pipeline().addLast(new WebSocketHandler());
                        }
                    });
            //绑定端口号
            ChannelFuture cf = sb.bind().sync();
            log.info("websocket已启动，正在监听： " + cf.channel().localAddress());
            channel = cf.channel().closeFuture().sync().channel();//这里绑定端口启动后，会阻塞线程，也就是为什么要用线程池启动的原因
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            group.shutdownGracefully().sync();// 释放线程池资源
            bossGroup.shutdownGracefully().sync();
            stop();
        }
    }
}
