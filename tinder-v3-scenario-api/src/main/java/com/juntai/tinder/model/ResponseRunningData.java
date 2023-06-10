package com.juntai.tinder.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName EtcdData
 * @Description
 * @Author nemo
 * @Date 2022/4/21 13:15
 * @Version
 **/
@Data
public class ResponseRunningData implements Serializable {

    private static final long serialVersionUID = -4948804279084464118L;

    private String simId;

    private String scenarioCode;

}
