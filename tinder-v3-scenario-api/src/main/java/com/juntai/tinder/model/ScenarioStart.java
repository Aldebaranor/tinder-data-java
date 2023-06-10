package com.juntai.tinder.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ScenarioStart
 * @Description
 * @Author ShiZuan
 * @Date 2022/7/1 11:17
 * @Version
 **/
@Data
public class ScenarioStart implements Serializable {

    private static final long serialVersionUID = 84531821793923354L;

    private String code;

    private Distribution distribution;

    private Boolean recordData = true;


}
