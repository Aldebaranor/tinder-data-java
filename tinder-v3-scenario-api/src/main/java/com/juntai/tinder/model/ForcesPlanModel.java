package com.juntai.tinder.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 想定文件计划$
 * @Author: nemo
 * @Date: 2022/5/24 8:58 AM
 */
@Data
public class ForcesPlanModel extends ForcesPlanBaseModel implements Serializable {

    private static final long serialVersionUID = 2844069772949695333L;
    private Long id;
    private String name;
    private Long simId;
    private Long forcesId;
    private String taskType;
    private Long startTime;
    private Long endTime;

}
