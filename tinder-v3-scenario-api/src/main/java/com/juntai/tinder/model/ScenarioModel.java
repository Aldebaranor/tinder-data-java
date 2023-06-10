package com.juntai.tinder.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 想定$
 * @Author: nemo
 * @Date: 2022/12/5 10:27 AM
 */
@Data
public class ScenarioModel implements Serializable {

    private static final long serialVersionUID = -5692679924546738821L;

    private Scenario scenario;

    private Distribution distribution;

}
