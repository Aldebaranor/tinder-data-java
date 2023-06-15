package com.juntai.tinder.model;


import com.juntai.tinder.entity.Experiment;
import com.juntai.tinder.entity.Forces;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 想定文件$
 * @Author: nemo
 * @Date: 2022/12/5 10:05 AM
 */
@Data
public class Scenario implements Serializable {

    private static final long serialVersionUID = 9170857533601538962L;
    private Experiment experiment;
    private List<Forces> forcesInfo;
    private List<GeometryModel> geometry;
    private List<ForcesPlanModel> plan;
}

