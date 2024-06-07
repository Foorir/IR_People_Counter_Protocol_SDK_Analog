package com.example.he2nb1.msg;

/**
 * @author TRH
 * @description:
 * @Package com.example.testdemo.tcpkeliu1.msg
 * @date 2023/3/23 16:42
 */
public enum CmdType {

    HEART_UPLOAD((byte) 0xD1, "Heartbeat reporting"),
    DATA_UPLOAD((byte) 0xD2, "Data reporting"),
    ;

    private byte typeCode;
    private String descs;

    CmdType(byte typeCode, String descs) {
        this.typeCode = typeCode;
        this.descs = descs;
    }

    public static String getDescsByCode(byte typeCode) {
        CmdType[] enums = CmdType.values();
        for (CmdType e : enums) {
            if (e.typeCode == typeCode) {
                return e.descs;
            }
        }
        return null;
    }

    public byte getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(byte typeCode) {
        this.typeCode = typeCode;
    }

    public String getDescs() {
        return descs;
    }

    public void setDescs(String descs) {
        this.descs = descs;
    }

}
