package com.juntai.tinder.model;

import lombok.Data;

import java.io.Serializable;


/**
 * @Description: 运动特征数据
 * @Author: nemo
 * @Date: 2022/3/23
 */
@Data
public class MoveData implements Serializable {


    private static final long serialVersionUID = -8466824406447929294L;

    /**
     * 1经度
     */
    private Double lon;
    /**
     * 2纬度
     */
    private Double lat;
    /**
     * 3高度
     */
    private Double alt;
    /**
     * 4航向角纬度
     */
    private Double heading;
    /**
     * 5横滚角
     */
    private Double roll;
    /**
     * 6俯仰角
     */
    private Double pitch;
    /**
     * 7速度
     */
    private Double speed;
    /**
     * 8生命值
     */
    private Double life;
    /**
     * 9剩余里程
     */
    private Double remainingMileage;

    @Override
    public String toString() {
        return String.format("%s_%s_%s_%s_%s_%s_%s", lon, lat, alt, heading, roll, pitch, speed, life, remainingMileage);
    }


}
