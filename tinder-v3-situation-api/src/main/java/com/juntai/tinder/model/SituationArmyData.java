package com.juntai.tinder.model;

import lombok.Data;

import java.io.Serializable;


/**
 * @Description:前端需要维护的兵力列表
 * @Author: nemo
 * @Date: 2022/3/25
 */
@Data
public class SituationArmyData implements Serializable {

    private static final long serialVersionUID = -3799025714574981598L;
    /**
     * 兵力ID
     */
    private String id;
    /**
     * 兵力名称
     */
    private String name;
    /**
     * 国家
     */
    private String country;
    /**
     * 生命值
     */
    private Integer life;
    /**
     * 敌我属性 0 红方，1，蓝方，2，白方
     */
    private String team;
    /**
     * 兵力分类
     */
    private String type;
    /**
     * 军标
     */
    private String iconArmy;
    /**
     * 三维图标
     */
    private String icon3dUrl;
    /**
     * 是否被对方阵营侦测到
     */
    private Integer beFound;
    /**
     * 被哪个阵营发现，0 红方，1，蓝方，
     */
    private String sourceTeam;
    /**
     * 敌我属性 0 红方，1，蓝方，
     */
    private String teamDetect;
    /**
     * 兵力分类
     */
    private String typeDetect;
    /**
     * 军标
     */
    private String iconArmyDetect;
    /**
     * 三维图标
     */
    private String icon3dDetect;


    @Override
    public String toString() {
        return String.format("%s@%s@%s@%s@%s@%s@%s@%s@%s@%s@%s@%s@%s@%s",
                id, name, country == null ? "" : country, life == null ? 0 : life, team, type, iconArmy == null ? "" : iconArmy, icon3dUrl == null ? "" : icon3dUrl,
                beFound, sourceTeam == null ? "" : sourceTeam, teamDetect == null ? "" : teamDetect, typeDetect == null ? "" : typeDetect, iconArmyDetect == null ? "" : iconArmyDetect, icon3dDetect == null ? "" : icon3dDetect);
    }

}
