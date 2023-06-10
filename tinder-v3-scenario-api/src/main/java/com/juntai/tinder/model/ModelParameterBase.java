package com.juntai.tinder.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 想定用到的模型参数$
 * @Author: nemo
 * @Date: 2022/5/23 10:09 PM
 */
@Data
public class ModelParameterBase implements Serializable {

    private static final long serialVersionUID = 1447932917304946102L;

    private String name = "";
    /**
     * 数据值/默认值
     */
    private String value = "";

}
