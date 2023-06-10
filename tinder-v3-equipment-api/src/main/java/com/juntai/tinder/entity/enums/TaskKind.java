package com.juntai.tinder.entity.enums;

import com.juntai.soulboot.common.enums.EnumDict;
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
@EnumDict(name="taskKindDict")
public enum TaskKind implements I18nEnum<String> {

    /**
     * 通用基本任务
     */
    BASIC("00"),
    /**
     * 巡逻任务
     */
    PATROL("01"),
    /**
     * 侦察任务
     */
    RECONNAISSANCE("02"),
    /**
     * 打击任务
     */
    HIT("03"),
    /**
     * 干扰任务
     */
    INTERFERENCE("04"),
    /**
     * 布防任务
     */
    DEFENCE("05"),
    /**
     * 清除任务
     */
    CLEAN("06"),

    /**
     * 补给任务
     */
    FEED("07")
    ;

    private final String value;
}
