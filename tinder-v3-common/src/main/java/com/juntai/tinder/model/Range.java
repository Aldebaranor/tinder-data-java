package com.juntai.tinder.model;

import lombok.Data;

import java.io.Serializable;


/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/3/25
 */
@Data
public class Range implements Serializable {

    private static final long serialVersionUID = 3401264938569975996L;
    private float start;
    private float end;
    private float up;
    private float down;

    @Override
    public String toString() {
        return String.format("[%s,%s,%s,%s]", start, end, up, down);
    }
}
