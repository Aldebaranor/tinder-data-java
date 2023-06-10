package com.juntai.tinder.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author nemo
 * @date: 2022/05/20/5:24 下午
 * @description:
 */
@Data
public class ForcesEntity implements Serializable {

    private static final long serialVersionUID = 6058370352782448321L;

    private String experimentId;

    private String team;

    private String modelId;

    private String equipmentId;

    private Integer num;


}
