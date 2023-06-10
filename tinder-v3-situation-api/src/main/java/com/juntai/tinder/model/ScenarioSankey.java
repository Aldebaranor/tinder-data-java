package com.juntai.tinder.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Administrator
 * @date 2023/3/13 17:10
 */

@Data
public class ScenarioSankey implements Serializable {
    private String nodeId;
    private String nodeName;
    private String simId;
    private String simName;
    private Long value;

}
