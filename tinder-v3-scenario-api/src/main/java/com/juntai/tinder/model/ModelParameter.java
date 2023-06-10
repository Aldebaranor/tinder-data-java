package com.juntai.tinder.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 想定用到的模型参数$
 * @Author: nemo
 * @Date: 2022/5/23 10:09 PM
 */
@Data
public class ModelParameter extends ModelParameterBase implements Serializable {

    private static final long serialVersionUID = 7011597609226589537L;

    private String text = "";

    private String datatype = "";
    /**
     * 数据单位
     */
    private String unit = "";
    /**
     * 数据范围
     */
    private String range = "";
    /**
     * 描述
     */
    private String desc = "";


}
