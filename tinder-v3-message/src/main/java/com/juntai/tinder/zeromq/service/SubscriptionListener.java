package com.juntai.tinder.zeromq.service;

/**
 * @Description: 监听$
 * @Author: nemo
 * @Date: 2022/6/25 2:49 PM
 */

@FunctionalInterface
public interface SubscriptionListener {

    //void onReceive(String s) throws InterruptedException;

    /**
     * ZMQ消费解析
     *
     * @param s1
     * @param b2
     * @throws InterruptedException
     */
    void onReceiveDouble(String s1, byte[] b2) throws InterruptedException;

}