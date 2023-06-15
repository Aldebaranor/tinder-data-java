package com.juntai.tinder.entity.enums;

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
public enum TeamType implements I18nEnum<String> {

    RED("0"),
    BLUE("1"),
    WHITE("2"),
    ;

    private final String value;

}
