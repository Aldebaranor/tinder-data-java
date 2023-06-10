package com.juntai.tinder.scenario;

import lombok.Data;

import java.io.Serializable;


/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/3/25
 */
@Data
public class ScenarioResult<T> implements Serializable {

    private static final long serialVersionUID = -3553803024450719799L;
    private Boolean hasError;
    private String message;
    private T result;

}
