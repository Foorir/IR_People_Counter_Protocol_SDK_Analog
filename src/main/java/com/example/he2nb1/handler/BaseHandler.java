package com.example.he2nb1.handler;

import com.example.he2nb1.msg.MsgVo;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author TRH
 * @description:
 * @Package com.example.he2nb1.handler
 * @date 2023/3/27 16:20
 */
public interface BaseHandler {

    MsgVo handle(MsgVo msgVo, ChannelHandlerContext ctx);

}
