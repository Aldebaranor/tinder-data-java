package com.juntai.tinder.model;

import com.juntai.tinder.entity.enums.GeometryType;
import lombok.Data;


/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/3/22
 */
@Data
public class GeometryModel {

    private static final long serialVersionUID = -7155912375888196913L;

    private String id;

    private String name;

    /**
     * "点线面类型：Point,LineString,Circle,Polygon，Sphere，Cylinder，Box"
     */
    private GeometryType geometryType;

    /**
     * "空间资源类型：0其他 1航线 2任务区 3禁区 4出发点 5到达点"
     */

    private String type;

    /**
     * 样式
     */
    private PropertiesStyle style;

    private Double[][] points;

    /**
     * "附加字段，如果是Circle和球，这里是半径"
     */
    private Double additional;


}
