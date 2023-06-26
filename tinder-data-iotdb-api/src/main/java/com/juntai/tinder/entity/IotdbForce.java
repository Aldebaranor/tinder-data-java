package com.juntai.tinder.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Song
 * @Date 2023/6/21 16:38
 */
@Data
public class IotdbForce implements Serializable {

    private static final long serialVersionUID = 4382143555514578604L;
    private Integer Stage;

    private Integer SimTime;

    private Double StepRatio;

    private List<IotdbForcePosture> Forces;
}
