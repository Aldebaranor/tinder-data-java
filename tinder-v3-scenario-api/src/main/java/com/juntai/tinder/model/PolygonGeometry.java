package com.juntai.tinder.model;

import com.egova.model.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/3/22
 */
@Data
public class PolygonGeometry extends BaseEntity {

    private static final long serialVersionUID = -7698627826913111552L;

    private String id;

    private String name;

    private String type;

    private List<Double> heights;

    private int team;

    private Boolean bePolygon;

    private PolygonGeometryData geometry;


    @Data
    public static class PolygonGeometryData implements Serializable {

        private static final long serialVersionUID = -452792985238441293L;

        private String type;
        private Double[][][] coordinates;
    }
}
