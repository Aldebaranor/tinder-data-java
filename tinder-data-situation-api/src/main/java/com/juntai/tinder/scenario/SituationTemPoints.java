package com.juntai.tinder.scenario;

import lombok.Data;

import java.io.Serializable;


/**
 * @Description: 运动特征数据
 * @Author: nemo
 * @Date: 2022/3/23
 */
@Data
public class SituationTemPoints implements Serializable {


    private static final long serialVersionUID = 768037215503485589L;
    private String id;
    private String instId;
    /**
     * 0 外推 1 航线
     */

    private Integer type;
    private Double[][] data;
    private Long time;


}
