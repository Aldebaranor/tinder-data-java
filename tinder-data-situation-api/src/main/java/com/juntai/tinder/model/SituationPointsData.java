package com.juntai.tinder.model;

import com.juntai.soulboot.util.JsonUtils;
import lombok.Data;

import java.io.Serializable;


/**
 * @Description: 点迹特效数据
 * @Author: nemo
 * @Date: 2022/3/23
 */
@Data
public class SituationPointsData implements Serializable {

    private static final long serialVersionUID = -2723138461948217173L;
    /**
     * ID
     */
    private String id;
    /**
     * 兵力ID
     */
    private String armyId;
    /**
     * 0-外推 1-线 2面 3-雷达波瓣
     */
    private String type;
    /**
     * 数据
     */
    private Double[][] points;

    @Override
    public String toString() {

        return String.format("%s@%s@%s@%s", id, armyId, type, JsonUtils.write(points));
    }


}
