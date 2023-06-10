package com.juntai.tinder.model;

import lombok.Data;

import java.io.Serializable;


/**
 * @Description: 运动特征数据
 * @Author: nemo
 * @Date: 2022/3/23
 */
@Data
public class Point implements Serializable {

    private static final long serialVersionUID = 7804344325211193972L;
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

}
