package com.juntai.tinder.entity.enums;

import com.juntai.soulboot.common.enums.EnumDict;
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
@EnumDict(name="userTypeDict")
public enum UserType implements I18nEnum<Integer> {

    ADMIN(0),

    USER(1),

    ;

    private final Integer value;

}
