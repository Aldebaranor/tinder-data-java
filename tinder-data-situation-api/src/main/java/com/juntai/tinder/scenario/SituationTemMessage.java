package com.juntai.tinder.scenario;

import lombok.Data;

import java.io.Serializable;


/**
 * @Description: 运动特征数据
 * @Author: nemo
 * @Date: 2022/3/23
 */
@Data
public class SituationTemMessage implements Serializable {

    private static final long serialVersionUID = 4591792201288357235L;
    private String id;
    private String team;
    private String instId;
    /**
     * 0 区域告警 1区域预警  2 文字特效
     */
    private Integer type;
    private String[] content;

    private Long time;


}
