package com.example.he2nb1.dispatcher;

import com.alibaba.fastjson.JSON;
import com.example.he2nb1.handler.BaseHandler;
import com.example.he2nb1.msg.CmdType;
import com.example.he2nb1.msg.MsgVo;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author TRH
 * @description: message distribution
 * @Package com.example.he2nb1.dispatcher
 * @date 2023/3/27 16:19
 */
@Slf4j
public class RequestDispatcher {
    private static Map<Byte, BaseHandler> requestHandlerMap= new ConcurrentHashMap<>();


    public  static void dispatcher(ChannelHandlerContext channelHandlerContext, MsgVo msgVo5g){
//        Get Message Processor Type
        byte code = msgVo5g.getType();
//
        String command = CmdType.getDescsByCode(code);
        log.info("{} - {} ===========>{}",String.format("%02x ", code), command, JSON.toJSONString(msgVo5g));

//		Selection of methods for subsequent business processing through the obtained code
        BaseHandler baseHandler = (BaseHandler) requestHandlerMap.get(code);
        if (baseHandler == null){
            String sTemp = Integer.toHexString(0xFF & code);
            log.info("Instruction does not exist: ======>{}",sTemp);
            return;
        }
        MsgVo responseMsg = baseHandler.handle(msgVo5g, channelHandlerContext);
        log.info("{} - {} ===========>{}", String.format("%02x ", code), "data response", JSON.toJSONString(responseMsg));
        if (responseMsg != null) {
//			If there is a return message, output the returned message
            channelHandlerContext.writeAndFlush(responseMsg);
        }
    }

    /**
     * When starting the project, store the code and corresponding processing class in the
     * @param courseMap
     */
    public static void setRequestHandlerMap(Map<Byte, BaseHandler> courseMap) {
        if (courseMap != null && courseMap.size() > 0) {
            for (Map.Entry<Byte, BaseHandler> entry : courseMap.entrySet()) {
                requestHandlerMap.put(entry.getKey(), entry.getValue());
            }
        }
    }
}
