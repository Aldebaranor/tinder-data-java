package com.juntai.tinder.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;


/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/3/22
 */
@Data
public class PointGeometry implements Serializable {

    private static final long serialVersionUID = -2209072530687594024L;
    private String type;

    private Double[] coordinates;

    @Override
    public String toString() {
        return "type=" + type + ", coordinates=" + Arrays.toString(coordinates);
    }
}
