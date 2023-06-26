package com.juntai.tinder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName LinePoints
 * @Description
 * @Author ShiZuan
 * @Date 2022/4/21 13:15
 * @Version
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SituationPoint implements Serializable {
    private static final long serialVersionUID = -7612165261938244880L;

    private Double lon;
    private Double lat;
    private Double alt;
    private Double radius;
    private String type;

    @Override
    public String toString() {
        return String.format("%s_%s_%s_%s_%s_%s", lon, lat, alt, radius, type, "");
    }

}
