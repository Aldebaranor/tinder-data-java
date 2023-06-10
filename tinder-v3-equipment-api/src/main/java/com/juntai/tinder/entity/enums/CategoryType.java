package com.juntai.tinder.entity.enums;
import com.juntai.soulboot.common.enums.EnumDict;
import com.juntai.soulboot.common.enums.I18nEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author ：huangkang
 * @date ： 2022/4/18 16:29
 * @description：分类类型枚举
 * @version: 1.0.0
 */
@Getter
@RequiredArgsConstructor
@EnumDict(name="categoryTypeDict")
public enum CategoryType implements I18nEnum<String> {

    /**
     * 行为组件模型
     */
    COMMON("1"),

    /**
     * 装备
     */
    EQUIPMENT("2"),
    /**
     * 兵力
     */
    ARM("3"),

    /**
     * 实装代理
     */
    REAL("4");

    private final String value;

    public static CategoryType findEnumByValueOrCode(String code) {
        for (CategoryType statusEnum : CategoryType.values()) {
            if (statusEnum.getValue().equals(code) || statusEnum.name().equals(code)) {
                //如果需要直接返回code则更改返回类型为String,return statusEnum.code;
                return statusEnum;
            }
        }
        throw new IllegalArgumentException("code is invalid");
    }
}
