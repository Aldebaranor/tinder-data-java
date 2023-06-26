package com.juntai.tinder.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ScenarioOperate
 * @Description
 * @Author ShiZuan
 * @Date 2022/7/1 11:17
 * @Version
 **/
@Data
public class ScenarioOperate implements Serializable {

    private static final long serialVersionUID = 5269823071071371215L;

    private String code;
    /**
     * 想定订单号
     */
    private String id;
    /**
     * 操作 1开始 2暂停 3继续 4停止 5加速 6减速
     */
    private Integer type;

}
