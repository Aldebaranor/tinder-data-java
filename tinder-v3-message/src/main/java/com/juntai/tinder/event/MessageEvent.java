package com.juntai.tinder.event;

import org.springframework.context.ApplicationEvent;

/**
 * @Description: event事件$
 * @Author: nemo
 * @Date: 2022/9/19 2:42 PM
 */
public class MessageEvent extends ApplicationEvent {

    private String message;

    public MessageEvent(Object source, String message) {
        super(source);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

