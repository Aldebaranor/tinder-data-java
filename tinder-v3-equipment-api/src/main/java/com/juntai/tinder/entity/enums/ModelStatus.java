package com.juntai.tinder.entity.enums;

import com.juntai.soulboot.common.enums.EnumDict;
import com.juntai.soulboot.common.enums.I18nEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author ：huangkang
 * @date ： 2022/4/18 16:29
 * @description：模型状态类型枚举
 * @version: 1.0.0
 */
@Getter
@RequiredArgsConstructor
@EnumDict(name = "modelDict")
public enum ModelStatus implements I18nEnum<String> {

    /**
     * 编辑
     */
    DRAFT("0"),
    /**
     * 发布
     */
    PUBLISH("1"),
    ;

    private final String value;
}
