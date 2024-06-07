package com.example.he2nb1.handler;

import com.example.he2nb1.dispatcher.RequestDispatcher;
import com.example.he2nb1.msg.MsgVo;
import com.example.he2nb1.msg.NettyChannelMap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author TRH
 * @description:
 * @Package com.example.testdemo.tcpkeliu1.handler
 * @date 2023/3/23 17:51
 */
@ChannelHandler.Sharable
@Component
public class ServerChannelHandlerAdapter extends ChannelInboundHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(ServerChannelHandlerAdapter.class);


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String uuid = ctx.channel().id().asLongText();
        logger.info("Client：" + ctx.channel().remoteAddress() +",Connected! UUID:" +uuid);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel channel = (SocketChannel) ctx.channel();
        String deviceId = NettyChannelMap.get(channel);
        if (deviceId != null) {
            NettyChannelMap.removeClient(deviceId);
            logger.warn("{} 下线", deviceId);
        }
        super.channelInactive(ctx);
        try {
            ctx.channel().close();
        } catch (Exception e) {
            logger.error("channelInactive异常", e);
        }

    }



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MsgVo vo = (MsgVo) msg;
        logger.info(vo.getData()+"|||||||");
        try {
//			将消息传入中间处理器中
            RequestDispatcher.dispatcher(ctx, vo);
        } catch (Exception e) {

            e.printStackTrace();
            logger.error("channelReadException:" + e.getLocalizedMessage());
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                // logger.info("TCP IdleStateEvent,超过5分钟未读取到终端数据，关闭链接");
                ctx.channel().close();
                logger.warn(NettyChannelMap.get((SocketChannel) ctx.channel()) + " 超过"+ 2+"分钟未心跳");
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }


}
