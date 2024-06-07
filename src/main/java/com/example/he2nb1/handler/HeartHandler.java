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
 * @description: heartbeat
 * @Package com.example.testdemo.tcpkeliu1.handler
 * @date 2023/3/23 17:19
 */
@Slf4j
@Component
@RequestHandler(type = CmdType.HEART_UPLOAD)
@RequiredArgsConstructor
public class HeartHandler<T>  implements BaseHandler {


    @Override
    public MsgVo handle(MsgVo msgVo, ChannelHandlerContext ctx) {
        log.info("Received heartbeat application data：{}", msgVo.getData());
        String data = msgVo.getData();
        String[] split = data.split(",");
//      设备当前时间
        LocalDateTime now = LocalDateTime.now();
        String time = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(now);
//      开始时间
        String startTime = "0800";
//      结束时间
        String endTime = "2000";
//      间隔
        Integer interval = 1;
        String sn = split[0];

        int isLevelUp = 0;
        String updateUrl = "0";
        int velocity = 0;
        int direction = 0;
        int length = 465;
        int recordPeriod = 5;
        boolean isDisable = false;

        return response(msgVo, 0, time, interval, startTime,endTime,velocity,direction,isLevelUp, updateUrl,length, isDisable, sn, recordPeriod);
    }

    /**
     * //[Status Code], [Timestamp], [Business Start Time], [Business End Time], [Upload Interval], [Record Period], [Statistics Direction], [Upgrade Flag], [Upgrade Link], [Reservation]
     * @return
     */
    public MsgVo response(MsgVo msgVo5g, int code, String time, Integer interval, String startTime,
                            String endTime, int velocity, int direction, int update, String updateUrl, int length, boolean isDisable, String sn,
                            int recordPeriod) {

        MsgVo msgvo = new MsgVo();
        msgvo.setType(msgVo5g.getType());
        msgvo.setParams(msgVo5g.getType());
        StringBuilder data = new StringBuilder();
        data.append(code);
        data.append(",");
        data.append(time);
        data.append(",");
        data.append(startTime);
        data.append(",");
        data.append(endTime);
        data.append(",");
        data.append(interval);
        data.append(",");
        data.append(recordPeriod);
        data.append(",");
        data.append(direction);
        data.append(",");
        data.append(update);

        if(update == 0){
            data.append(",0");
        }else{
            data.append(",");
            data.append(updateUrl);
            data.append(",");
            data.append(length);
        }

        if(isDisable) {
            data.append(",1");
        }else{
            data.append(",0");
        }

        String s = data.toString();
        msgvo.setData(s);
        msgvo.setLen(s.length());
        msgvo.setCrcHigh(msgVo5g.getCrcHigh());
        msgvo.setCrcLow(msgVo5g.getCrcLow());

        try{
            log.info(sn + "reply：" + s);
            log.info(sn + "reply：" + msgvo.toHexString());
        }catch (Exception e){
            e.printStackTrace();
        }

        return msgvo;
    }
}
