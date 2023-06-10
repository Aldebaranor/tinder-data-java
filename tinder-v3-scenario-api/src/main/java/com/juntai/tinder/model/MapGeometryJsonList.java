package com.juntai.tinder.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/5/23
 */
@Data
public class MapGeometryJsonList implements Serializable {

    private static final long serialVersionUID = 6312758669114749330L;
    private String experimentId;
    private String team;
    private List<String> json;

}
