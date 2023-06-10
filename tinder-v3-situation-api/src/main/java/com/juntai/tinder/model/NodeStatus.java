package com.juntai.tinder.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Administrator
 * @date 2023/3/13 16:35
 */
@Data
public class NodeStatus implements Serializable {

    private Long nodeId;
    private String name;
    private String ip;
    private String cpu;
    private Float cpuRate;
    private String disk;
    private Float diskTotal;
    private Float diskUsed;
    private Float diskRate;
    private String memory;
    private Float memoryRate;
    private Float memoryTotal;
    private Float memoryUsed;

}
