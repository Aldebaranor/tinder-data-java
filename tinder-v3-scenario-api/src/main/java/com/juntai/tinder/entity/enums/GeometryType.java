package com.juntai.tinder.entity.enums;

import com.juntai.soulboot.common.enums.I18nEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author ：nemo
 * @date ： 2022/4/18 16:29
 * @description：
 * @version: 1.0.0
 */

@Getter
@RequiredArgsConstructor
public enum GeometryType implements I18nEnum<Integer> {

    /**
     * 点
     */
    POINT(0),
    /**
     * 线
     */
    LINESTRING(1),
    /**
     * 圆
     */
    CIRCLE(2),
    /**
     * 多边形
     */
    POLYGON(3),
    /**
     * 球
     */
    SPHERE(4),
    /**
     * 圆柱
     */
    CYLINDER(5),
    /**
     * 体
     */
    BOX(6),
    ;

    private final Integer value;

}



