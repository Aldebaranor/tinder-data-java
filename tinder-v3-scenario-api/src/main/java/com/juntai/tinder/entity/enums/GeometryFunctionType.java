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
public enum GeometryFunctionType implements I18nEnum<Integer> {

    /**
     * 其他
     */
    OTHER(0),
    /**
     * 任务区
     */
    MISSION_AREA(2),
    WOMAN(2),
    /**
     * 禁区
     */
    PENALTY_ZONE(3),
    /**
     * 出发点
     */
    DEPARTURE_AREA(4),
    /**
     * 到达点
     */
    ARRIVAL_AREA(5),
    ;

    private final Integer value;

}

