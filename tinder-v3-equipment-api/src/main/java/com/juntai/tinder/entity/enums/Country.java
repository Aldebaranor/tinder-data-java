package com.juntai.tinder.entity.enums;

import com.juntai.soulboot.common.enums.EnumDict;
import com.juntai.soulboot.common.enums.I18nEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author chendb
 * @description:
 * @date 2020-11-07 00:28:03
 */

@Getter
@RequiredArgsConstructor
@EnumDict(name = "countryDict")
public enum Country implements I18nEnum<String> {


    /**
     * 中国
     */
    CHN("CHN"),
    /**
     * 俄罗斯
     */
    RUS("RUS"),
    /**
     * 美国
     */
    USA("USA"),
    /**
     * 法国
     */
    FRA("FRA"),
    /**
     * 英国
     */
    GBR("GBR"),
    /**
     * 德国
     */
    DEU("DEU"),
    /**
     * 印度
     */
    IND("IND"),
    /**
     * 日本
     */
    JPN("JPN"),
    /**
     * 韩国
     */
    KOR("KOR"),
    /**
     * 朝鲜
     */
    PRK("PRK"),
    /**
     * 土耳其
     */
    TUR("TUR"),
    /**
     * 巴基斯坦
     */
    PAK("PAK"),
    /**
     * 澳大利亚
     */
    AUS("AUS"),
    /**
     * 意大利
     */
    ITA("ITA"),
    /**
     * 以色列
     */
    ISR("ISR"),
    /**
     * 中国台湾
     */
    TWN("TWN"),
    /**
     * 伊朗
     */
    IRN("IRN"),
    /**
     * 沙特阿拉伯
     */
    SAU("SAU"),
    /**
     * 巴西
     */
    BRA("BRA"),
    /**
     * 埃及
     */
    EGY("EGY"),
    /**
     * 乌克兰
     */
    UKR("UKR"),
    /**
     * 波兰
     */
    POL("POL"),
    /**
     * 泰国
     */
    THA("THA"),
    /**
     * 越南
     */
    VNM("VNM");

    private final String value;
}
