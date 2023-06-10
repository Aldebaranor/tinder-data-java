package com.juntai.tinder.model;

import com.juntai.soulboot.util.JsonUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @Description: 态势分发数据
 * @Author: nemo
 * @Date: 2022/3/23
 */
@Data
public class SituationGeometryData implements Serializable {

    private static final long serialVersionUID = 5472612212636799414L;

    /**
     * 实体ID
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 阵营 0红方，1蓝方
     */
    private String team;

    /**
     * 动态线类型 1.点,2线,3面,4圆
     */
    private int type;

    /**
     * 连线数据
     */
    private List<SituationPoint> properties;



    @Override
    public String toString() {
        List<String> resultList = properties.stream().map(situationPoint -> {
            return situationPoint.toString();
        }).collect(Collectors.toList());
        return String.format("%s@%s@%s@%s@%s@%s", id, name, team, type, "", JsonUtils.write(resultList));
    }

}
