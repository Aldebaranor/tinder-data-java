package com.juntai.tinder.netty;

import com.juntai.tinder.config.MetaConfig;
import com.juntai.tinder.netty.handler.NettyUdpClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.InternetProtocolFamily;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.NetUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;


/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/3/23
 */
@Data
@Component
@Configuration
@Slf4j
@ConfigurationProperties(prefix = "netty")
@ConditionalOnProperty(prefix = "netty.udpGroup.client", name = "enable", havingValue = "true", matchIfMissing = false)
public class NettyUdpGroupClient {

    private Bootstrap bootstrap = new Bootstrap();

    private NioEventLoopGroup group = new NioEventLoopGroup();

    private NioDatagramChannel channel;

    private String groupIp;

    private Integer clientPort;

    @Autowired
    private UnpackMassageFactory factory;

    @Autowired
    private MetaConfig metaConfig;

    @PostConstruct
    public void start() throws InterruptedException {
        //只使用IPV4
        System.setProperty("java.net.preferIPv4Stack", "true");
        //InetSocketAddress groupAddress = new InetSocketAddress("ff00::ef01:1ff", 7778);
        InetSocketAddress groupAddress = new InetSocketAddress(groupIp, clientPort);

        NetworkInterface ni = NetUtil.LOOPBACK_IF;
        Enumeration<InetAddress> addresses = ni.getInetAddresses();
        InetAddress localAddress = null;
        while (addresses.hasMoreElements()) {
            InetAddress address = addresses.nextElement();
            if (address instanceof Inet4Address) {
                localAddress = address;
            }
        }
        bootstrap.group(group)
                .channelFactory(new ChannelFactory<Channel>() {
                    @Override
                    public NioDatagramChannel newChannel() {
                        return new NioDatagramChannel(InternetProtocolFamily.IPv4);
                    }
                })
                //.channel(NioDatagramChannel.class)
                // 设置LocalAddress
                .localAddress(localAddress, groupAddress.getPort())
                // 设置Option 组播
                .option(ChannelOption.IP_MULTICAST_IF, ni)
                // 设置Option 重复地址
                .option(ChannelOption.SO_REUSEADDR, true)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new NettyUdpClientHandler(factory, metaConfig));
                    }
                });


        channel = (NioDatagramChannel) bootstrap.bind(groupAddress.getPort()).sync().channel();
        channel.joinGroup(groupAddress, ni).sync();
        log.info("----------------------------UdpGroupClient start success");
    }

    @PreDestroy
    public void destory() throws InterruptedException {
        group.shutdownGracefully().sync();
        log.info("----------------------------关闭Netty");
    }


}