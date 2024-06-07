package com.example.he2nb1.msg;

import com.example.he2nb1.coder.ByteUtil;

/**
 * @author TRH
 * @description:
 * @Package com.example.he2nb1.msg
 * @date 2023/3/27 16:12
 */
public class MsgVo {

    private byte head = (byte) 0x7E;
    private byte[] sn = new byte[] { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
    private byte type;
    private byte params;
    private int len;
    private String data;
    private byte    crcLow;
    private byte    crcHigh;
    private Byte   tail= (byte) 0x7D;

    public MsgVo() {
    }

    public String toHexString(){
        String headStr = ByteUtil.bytesToHexString(new byte[]{head});
        String snStr = ByteUtil.bytesToHexString(sn);
        String typeStr = ByteUtil.bytesToHexString(new byte[]{type});
        String paramsStr = ByteUtil.bytesToHexString(new byte[]{params});
        String lenStr = ByteUtil.bytesToHexString(ByteUtil.toLH(len));
        String dataStr = ByteUtil.bytesToHexString(data.getBytes());
        String crcLowStr = ByteUtil.bytesToHexString(new byte[]{crcLow});
        String crcHighStr = ByteUtil.bytesToHexString(new byte[]{crcHigh});
        String tailStr = ByteUtil.bytesToHexString(new byte[]{tail});

        return headStr + snStr + typeStr + paramsStr + lenStr + dataStr + crcLowStr + crcHighStr + tailStr;

    }

    public MsgVo(byte head, byte[] sn, byte type, byte params, int len, String data, byte crcLow, byte crcHigh, Byte tail) {
        this.head = head;
        this.sn = sn;
        this.type = type;
        this.params = params;
        this.len = len;
        this.data = data;
        this.crcLow = crcLow;
        this.crcHigh = crcHigh;
        this.tail = tail;
    }

    public byte getHead() {
        return head;
    }

    public void setHead(byte head) {
        this.head = head;
    }

    public byte[] getSn() {
        return sn;
    }

    public void setSn(byte[] sn) {
        this.sn = sn;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getParams() {
        return params;
    }

    public void setParams(byte params) {
        this.params = params;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public byte getCrcLow() {
        return crcLow;
    }

    public void setCrcLow(byte crcLow) {
        this.crcLow = crcLow;
    }

    public byte getCrcHigh() {
        return crcHigh;
    }

    public void setCrcHigh(byte crcHigh) {
        this.crcHigh = crcHigh;
    }

    public Byte getTail() {
        return tail;
    }

    public void setTail(Byte tail) {
        this.tail = tail;
    }
}
