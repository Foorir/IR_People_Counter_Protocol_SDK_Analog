package com.example.he2nb1.annotation;

import com.example.he2nb1.msg.CmdType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author TRH
 * @description: Processor annotations
 * @Package com.example.he2nb1.annotation
 * @date 2023/3/27 16:10
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface RequestHandler {

    CmdType type();
}
