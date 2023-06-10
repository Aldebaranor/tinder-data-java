package com.juntai.tinder.config.entity.enums;


import lombok.Getter;


/**
 * @Description: 操作字典
 * @Author: nemo
 * @Date: 2022/5/24 2:24 PM
 */

@Getter
public enum OperateType {

    /**
     * 开始
     */
    START(1, "开始"),
    /**
     * 暂停
     */
    PAUSE(2, "暂停"),
    /**
     * 继续
     */
    CONTINUE(3, "继续"),
    /**
     * 停止
     */
    STOP(4, "停止"),
    /**
     * 加速
     */
    ACCELERATE(5, "加速"),
    /**
     * 减速
     */
    DECELERATE(6, "减速");

    private Integer value;
    private String text;

    OperateType(Integer value, String text) {
        this.value = value;
        this.text = text;
    }

}
