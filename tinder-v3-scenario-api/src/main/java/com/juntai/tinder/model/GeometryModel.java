package com.juntai.tinder.model;


import com.egova.model.BaseEntity;
import com.egova.model.annotation.Display;
import com.soul.tinder.entity.enums.GeometryType;
import lombok.Data;


/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/3/22
 */
@Data
public class GeometryModel extends BaseEntity {

    private static final long serialVersionUID = -7155912375888196913L;
    @Display("主键")
    private String id;

    private String name;

    @Display("点线面类型：Point,LineString,Circle,Polygon，Sphere，Cylinder，Box")
    private GeometryType geometryType;

    @Display("空间资源类型：0其他 1航线 2任务区 3禁区 4出发点 5到达点")
    private String type;

    @Display("样式")
    private PropertiesStyle style;

    private Double[][] points;

    @Display("附加字段，如果是Circle和球，这里是半径")
    private Double additional;


}
