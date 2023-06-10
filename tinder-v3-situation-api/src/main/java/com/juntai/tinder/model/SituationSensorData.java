package com.juntai.tinder.model;

import com.juntai.soulboot.util.JsonUtils;
import lombok.Data;

import java.io.Serializable;


/**
 * @Description: 雷达特效数据
 * @Author: nemo
 * @Date: 2022/3/23
 */
@Data
public class SituationSensorData implements Serializable {

    private static final long serialVersionUID = 7479010497512160070L;
    /**
     * 特效Id
     */
    private String id;
    /**
     * 兵力ID
     */
    private String armyId;
    /**
     * 开关
     */
    private Integer open;
    /**
     * 类型 0雷达，1红外，2，无源，3干扰，4通信覆盖范围
     */
    private String type;
    /**
     * 包络数据
     */
    private String[][] points;

    @Override
    public String toString() {

        return String.format("%s@%s@%s@%s@%s@%s", id, armyId, open, type, "", JsonUtils.write(points));
    }



}
