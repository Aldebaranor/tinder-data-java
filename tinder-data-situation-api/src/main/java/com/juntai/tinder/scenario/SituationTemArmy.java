package com.juntai.tinder.scenario;

import lombok.Data;

import java.io.Serializable;


/**
 * @Description: 运动特征数据
 * @Author: nemo
 * @Date: 2022/3/23
 */
@Data
public class SituationTemArmy implements Serializable {


    private static final long serialVersionUID = -4454606732749045479L;
    private String id;
    private String name;
    /**
     * 兵力装备ID
     */
    private String equipmentId;

    private String team;

    private Long time;


}
