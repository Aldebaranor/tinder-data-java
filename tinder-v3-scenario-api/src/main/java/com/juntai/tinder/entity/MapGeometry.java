package com.juntai.tinder.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.juntai.tinder.entity.enums.GeometryFunctionType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author nemo
 * @since 2023-06-07
 */
@Getter
@Setter
@TableName("meta_map_geometry")
public class MapGeometry implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private String id;

    /**
     * 试验编号
     */
    private String experimentId;

    /**
     * 阵营
     */
    private String team;

    /**
     * 名称
     */
    private String name;

    /**
     * 空间资源功能类型 0其他 1航线 2任务区 3禁区 4出发点 5到达点
     */
    private GeometryFunctionType type;

    /**
     * 几何形状，用于前端还原
     */
    private String geometry;

    /**
     * 点线面类型：Point,LineString,Polygon
     */
    private String geometryType;

    /**
     * 标绘类型,Point，Polyline，Curve，Arc，Circle，FreeHandLine，RectAngle，Ellipse，Lune，Sector，ClosedCurve，Polygon，FreePolygon,GatheringPlace，DoubleArrow，StraightArrow，AttackArrow，FineArrow，TailedAttackArrow，SquadCombat，TailedSquadCombat,RectFlag，TriangleFlag，CurveFlag
     */
    private String propertiesType;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime modifyTime;
}
