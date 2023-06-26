package com.juntai.tinder.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 * @date 2023/3/13 17:10
 */

@Data
public class ScenarioStatus implements Serializable {
    private String name;
    private String scenarioCode;
    private Long simId;
    private Boolean beRunning;
    private String duration;
    private Long forcesCount;
    private Long time;
    private Float progressRate;
    private String imageUrl;
    private List<PropertyItem<Long>> nodeList;

}
