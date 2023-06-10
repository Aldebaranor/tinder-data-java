package com.juntai.tinder.scenario;

import lombok.Data;

import java.io.Serializable;


/**
 * @Description: 运动特征数据
 * @Author: nemo
 * @Date: 2022/3/23
 */
@Data
public class SituationTemArea implements Serializable {

    private static final long serialVersionUID = -726279912499710229L;
    private String id;
    private Integer type;
    /**
     * 大于2个点被任务为多边形，等于两个点被任务为圆[[lon,lat,alt],[radius，0，0]]，一个点被认为是点
     */
    private Double[][] data;
    private Long time;


}
