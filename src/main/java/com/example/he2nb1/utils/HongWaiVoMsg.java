package com.example.he2nb1.utils;

import lombok.Data;

/**
 * @author TRH
 * @description:
 * @Package com.example.he2nb1.utils
 * @date 2023/3/27 16:28
 */
@Data
public class HongWaiVoMsg {
    private Integer update;
    private String time;
    private String updateUrl;
    private String startTime;
    private String endTime;
    private int code;
    private Integer interval;
    private String msg;
}
