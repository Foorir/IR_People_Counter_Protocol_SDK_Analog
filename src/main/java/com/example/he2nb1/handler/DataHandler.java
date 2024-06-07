package com.example.he2nb1.handler;

import com.example.he2nb1.annotation.RequestHandler;
import com.example.he2nb1.msg.CmdType;
import com.example.he2nb1.msg.MsgVo;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * @author TRH
 * @description: Data reporting
 * @Package com.example.testdemo.tcpkeliu1.handler
 * @date 2023/3/23 17:45
 */
@Component
@RequestHandler(type = CmdType.DATA_UPLOAD)
@Slf4j
@RequiredArgsConstructor
public class DataHandler implements BaseHandler {


    @Override
    public MsgVo handle(MsgVo msgVo, ChannelHandlerContext ctx) {

        String data = msgVo.getData();
        log.info("Received data report：{}", data);
        String[] split = data.split(",");
//sn
        String sn = split[0];

        LocalDateTime now = LocalDateTime.now();
        String time = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(now);

        int isLevelUp = 0;
        String updateUrl = "0";
        int length = 465;
//        [SN], [timestamp], [number of people in/out], [received power], [sent power], [code status], [version], [product]
        return response(msgVo, 0, time, isLevelUp, updateUrl, length);
    }


    public MsgVo response(MsgVo msgVo5g, int code, String time, int update, String updateUrl, int length) {
        MsgVo msgvo = new MsgVo();
        msgvo.setType(msgVo5g.getType());
        msgvo.setParams(msgVo5g.getType());
        StringBuilder data = new StringBuilder();
        data.append(code);
        data.append(",");
        data.append(time);
        data.append(",");
        data.append(update);
        data.append(",");
        data.append(updateUrl);
        data.append(",");
        data.append(length);

        String s = data.toString();
        msgvo.setData(s);
        msgvo.setLen(s.length());
        msgvo.setCrcHigh(msgVo5g.getCrcHigh());
        msgvo.setCrcLow(msgVo5g.getCrcLow());
        return msgvo;
    }
}
