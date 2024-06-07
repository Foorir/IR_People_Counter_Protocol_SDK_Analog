package com.example.he2nb1.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.he2nb1.coder.NettyDecoder;
import com.example.he2nb1.coder.NettyEncoder;
import com.example.he2nb1.handler.ServerChannelHandlerAdapter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class NettyTcpServer {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    private ServerBootstrap server;

    private ChannelFuture channelFuture;

    @Value("${netty.port}")
    private Integer port ;

    /**
     * channel adapter
     */
    @Resource
    private ServerChannelHandlerAdapter channelHandlerAdapter;

    public NettyTcpServer() {
        this.bossGroup = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup();
        server = new ServerBootstrap();
        server.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(905969674,7,2,3,0));
                        ch.pipeline().addLast(new NettyDecoder());
                        ch.pipeline().addLast(new NettyEncoder());
                        ch.pipeline().addLast(channelHandlerAdapter);
                    }
                });
        server.option(ChannelOption.SO_BACKLOG, 128);
        server.childOption(ChannelOption.SO_KEEPALIVE, true);
    }

    public synchronized void startListen() {
        try {
            channelFuture = server.bind(port).sync();
            log.info("server start on [{}] success", port);
        } catch (Exception e) {
            log.error("server start on [{}] fail", port);
            e.printStackTrace();
        }
    }


}
