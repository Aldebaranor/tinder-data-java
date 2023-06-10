package com.juntai.tinder.model;

import lombok.Data;

import java.io.Serializable;


/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/5/23
 */
@Data
public class MapGeometryJson implements Serializable {

    private static final long serialVersionUID = 359504631868837231L;
    private String experimentId;
    private String team;
    private String json;
    private String simId;
}
