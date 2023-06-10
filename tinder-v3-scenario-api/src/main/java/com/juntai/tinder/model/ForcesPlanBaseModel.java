package com.juntai.tinder.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 想定文件计划$
 * @Author: nemo
 * @Date: 2022/5/24 8:58 AM
 */
@Data
public class ForcesPlanBaseModel implements Serializable {

    private static final long serialVersionUID = -5774726576117867987L;
    /**
     * 资源ID，有资源ID仿真引擎将不会解析points
     */
    private String pointsId;
    private List<Point> points;
    private List<Long> targets;
    private List<Long> executors;
    private List<PropertyItem<String>> attributes;

}
