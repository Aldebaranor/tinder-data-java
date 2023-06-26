package com.juntai.tinder.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * @Description: 运动特征数据
 * @Author: nemo
 * @Date: 2022/3/23
 */
@Data
public class Polygon implements Serializable {

    private static final long serialVersionUID = 912110138947295731L;
    private String id;
    private String name;
    private List<Point> points;

}
