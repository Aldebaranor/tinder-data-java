package com.juntai.tinder.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author: 码头工人
 * @Date: 2021/08/25/4:39 下午
 * @Description:
 */

public class MessageProtocol implements Serializable {
    /**
     * 消息的开头的信息标志
     */
    private int headData;
    /**
     * 消息的长度
     */
    private int contentLength;
    /**
     * 消息的内容
     */
    private byte[] content;


    public MessageProtocol(int head, int contentLength, byte[] content) {
        this.contentLength = contentLength;
        this.content = content;
    }

    public int getHeadData() {
        return headData;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "SmartCarProtocol [head_data=" + headData + ", contentLength="
                + contentLength + ", content=" + Arrays.toString(content) + "]";
    }

    public byte[] toByteArray() {
        ByteBuf buf = Unpooled.buffer(32);
        buf.writeInt(headData);
        buf.writeInt(getContentLength());
        buf.writeBytes(getContent());
        return buf.array();
    }

    public ByteBuf toByteBuf() {
        ByteBuf buf = Unpooled.buffer(32);
        buf.writeInt(headData);
        buf.writeInt(getContentLength());
        buf.writeBytes(getContent());
        return buf;
    }


}
