package com.juntai.tinder.utils;

import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/5/23
 */
public class UnPackUtils {

    public static String getName(ByteBuf nameByteBuf) {

        for (int i = 0; i < nameByteBuf.readableBytes(); i++) {
            if (nameByteBuf.getByte(i) == 0) {
                String gbk = nameByteBuf.readBytes(i).toString(Charset.forName("GBK"));
                nameByteBuf.release();
                return gbk;
            }
        }
        return nameByteBuf.toString(Charset.forName("GBK"));
    }
}
