package com.example.he2nb1.coder;

import com.example.he2nb1.msg.MsgVo;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author TRH
 * @description:
 * @Package com.example.he2nb1.coder
 * @date 2023/3/27 16:11
 */
@Slf4j
public class NettyDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        byte[] bytes = new byte[in.readableBytes()];
        in.duplicate().readBytes(bytes);
        String error = bytesToHexString(bytes);
        log.info(error);

        byte head = in.readByte();
        ByteBuf sbuf = in.readBytes(4);
        byte[] sn = new byte[sbuf.readableBytes()];
        byte type = in.readByte();
        byte params = in.readByte();
        short len = in.readShort();
        ByteBuf bodybuf = in.readBytes(len);
        byte[] req = new byte[bodybuf.readableBytes()];
        bodybuf.readBytes(req);
        String body = null;
        body = new String(req, "UTF-8");
        byte crcLow = in.readByte();
        byte crcHigh = in.readByte();
        byte tail = in.readByte();
        MsgVo msgVo = new MsgVo();
        msgVo.setHead(head);
        msgVo.setSn(sn);
        msgVo.setType(type);
        msgVo.setParams(params);
        msgVo.setLen(len);
        msgVo.setData(body);
        msgVo.setCrcLow(crcLow);
        msgVo.setCrcHigh(crcHigh);
        msgVo.setTail(tail);

        System.err.println("--------type----------");
        System.err.println(len);
        out.add(msgVo);
        //释放资源
        sbuf.release();
        bodybuf.release();
    }



    /**
     * 数组转换成十六进制字符串
     *
     * @param bArray
     * @return HexString
     */
    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

}
