package com.juntai.tinder.entity.enums;

import com.juntai.soulboot.common.enums.EnumDict;
import com.juntai.soulboot.common.enums.I18nEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 资产类型
 *
 * @author 奔波儿灞
 * @since 1.0.0
 */
@Getter
@RequiredArgsConstructor
@EnumDict(name = "equipmentKindDict")
public enum EquipmentKind implements I18nEnum<String> {

    /**
     * 传感器/电子战
     */
    SENSOR("0"),
    /**
     * 武器系统
     */
    WEAPON("1"),
    /**
     * 电子对抗
     */
    ELECTRONIC("2"),
    /**
     * 通信
     */
    COMM("3"),
    /**
     * 导航
     */
    NAVIGATION("4"),
    /**
     * 推进系统
     */
    PROPULSION("5");
    private final String value;
}
