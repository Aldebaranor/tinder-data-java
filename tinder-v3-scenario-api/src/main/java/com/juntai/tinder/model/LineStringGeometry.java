package com.juntai.tinder.model;

import com.egova.model.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/5/23
 */
@Data
public class LineStringGeometry extends BaseEntity {

    private static final long serialVersionUID = -7245951426445646191L;
    private String id;

    private String name;

    private String type;

    private List<Double> heights;

    private int team;

    private Boolean bePolygon;

    private LineStringGeometryData geometry;


    @Data
    public static class LineStringGeometryData implements Serializable {

        private static final long serialVersionUID = -1336590888129203965L;

        private String type;

        private Double[][] coordinates;
    }
}
