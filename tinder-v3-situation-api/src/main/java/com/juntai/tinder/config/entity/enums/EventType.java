package com.juntai.tinder.config.entity.enums;

import com.juntai.soulboot.common.enums.I18nEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 枚举
 *
 * @author 奔波儿灞
 * @version 1.0.0
 */
@Getter
@RequiredArgsConstructor
public enum EventType implements I18nEnum<Integer> {

    /**
     * 兵力添加
     */
    ARMY_ADD(10),
    /**
     * 兵力销毁
     */
    ARMY_DELETE(11),
    /**
     *兵力下线
     */
    ARMY_OFFLINE(12),
    /**
     *兵力被发现
     */
    ARMY_FOUND(13),
    /**
     *传感器生成
     */
    SENSOR_ADD(20),
    /**
     *传感器关
     */
    SENSOR_DELETE(21),
    /**
     *传感器变更（变化）
     */
    SENSOR_CHANGE(22),
    /**
     *告警生成
     */
    MESSAGE_ADD(30),
    /**
     *告警销毁
     */
    MESSAGE_DELETE(31),
    /**
     *告警变化
     */
    MESSAGE_CHANGE(32),
    /**
     *区域生成
     */
    AREA_ADD(40),
    /**
     *区域销毁
     */
    AREA_DELETE(41),
    /**
     *区域变化
     */
    AREA_CHANGE(42),
    /**
     *数据链路生成
     */
    LINK_ADD(50),
    /**
     *数据链路销毁
     */
    LINK_DELETE(51),
    /**
     *数据链路变化
     */
    LINK_CHANGE(52),
    /**
     *点线生成
     */

    POINTS_ADD(60),
    /**
     *点线销毁
     */

    POINTS_DELETE(61),
    /**
     *点线变化
     */

    POINTS_CHANGE(62);

    private final Integer value;

}
