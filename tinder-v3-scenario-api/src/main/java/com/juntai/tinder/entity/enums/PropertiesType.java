package com.juntai.tinder.entity.enums;

import com.juntai.soulboot.common.enums.I18nEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @Description: 标绘类型$
 * @Author: nemo
 * @Date: 2022/5/24 3:40 PM
 */

@Getter
@RequiredArgsConstructor
public enum PropertiesType implements I18nEnum<String> {

    /**
     * Point，Polyline，Curve，Arc，Circle，FreeHandLine，RectAngle，Ellipse，Lune，Sector，ClosedCurve，Polygon，FreePolygon
     * GatheringPlace，DoubleArrow，StraightArrow，AttackArrow，FineArrow，TailedAttackArrow，SquadCombat，TailedSquadCombat
     * RectFlag，TriangleFlag，CurveFlag
     */

    /**
     * 点
     */
    POINT("Point"),
    /**
     * 线
     */
    POLYLINE("Polyline"),
    /**
     *曲线
     */
    CURVE("Curve"),
    /**
     *弧
     */
    ARC("Arc"),
    /**
     *自由线
     */
    FREE_HAND_LINE("FreeHandLine"),
    /**
     *圆
     */
    CIRCLE("Circle"),
    /**
     *多边形
     */
    POLYGON("Polygon"),
    /**
     *矩形
     */
    RECTANGLE("RectAngle"),
    /**
     *自由面
     */
    FREE_POLYGON("FreePolygon"),
    /**
     *椭圆
     */
    ELLIPSE("Ellipse"),
    /**
     *弧面
     */
    LUNE("Lune"),
    /**
     *扇形
     */
    SECTOR("Sector"),
    /**
     *闭合曲面
     */
    CLOSED_CURVE("ClosedCurve"),

    ;

    private final String value;


}
