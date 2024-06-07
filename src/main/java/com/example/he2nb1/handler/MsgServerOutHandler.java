package com.example.he2nb1.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

/**
 * @author TRH
 * @Package com.example.testdemo.tcpkeliu1.handler
 * @date 2023/3/24 15:07
 */
public class MsgServerOutHandler extends ChannelOutboundHandlerAdapter {
    private Logger logger = LoggerFactory.getLogger(MsgServerOutHandler.class);

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress,
                        SocketAddress localAddress, ChannelPromise promise)
            throws Exception {
        logger.warn("connect .... ");
        super.connect(ctx, remoteAddress, localAddress, promise);
    }
}
