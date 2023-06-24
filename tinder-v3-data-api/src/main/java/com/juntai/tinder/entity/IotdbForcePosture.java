package com.juntai.tinder.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Song
 * @Date 2023/6/21 16:05
 */
@Data
public class IotdbForcePosture implements Serializable {

    private static final long serialVersionUID = -5500976670122511338L;
    /**
     * 兵力Id
     */
    public Integer ForceId;
    /**
     * 生命值
     */
    public Double Life;
    /**
     * 经度
     */
    public Double Lon;
    /**
     * 纬度
     */
    public Double Lat;
    /**
     * 高度
     */
    public Double Alt;
    /**
     * 方向角
     */
    public Double Heading;
    /**
     * 倾斜角
     */
    public Double Pitch;
    /**
     * 转动角
     */
    public Double Roll;
    /**
     * 速度
     */
    public Double Speed;
    /**
     * 剩余里程数
     */
    public Double RemainingMileage;

    /**
     * 被探测生命值
     */
    public Double Life2;
    /**
     * 被探测经度
     */
    public Double Lon2;
    /**
     * 被探测纬度
     */
    public Double Lat2;
    /**
     * 被探测高度
     */
    public Double Alt2;
    /**
     * 被探测方向角
     */
    public Double Heading2;
    /**
     * 被探测倾斜角
     */
    public Double Pitch2;
    /**
     * 被探测转动角
     */
    public Double Roll2;
    /**
     * 被探测速度
     */
    public Double Speed2;



}
