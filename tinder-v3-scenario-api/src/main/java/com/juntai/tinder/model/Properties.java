package com.juntai.tinder.model;

import lombok.Data;

import java.io.Serializable;


/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/3/22
 */
@Data
public class Properties implements Serializable {

    private static final long serialVersionUID = -6589851331686889555L;
    private String type;
    private Double[][] points;
    private PropertiesStyle style;
}
