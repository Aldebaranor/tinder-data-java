package com.juntai.tinder.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.List;

import com.juntai.tinder.model.ModelParameterBase;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 搭载母体
 * </p>
 *
 * @author nemo
 * @since 2023-06-07
 */
@Getter
@Setter
@TableName("meta_forces")
public class Forces implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private String id;

    /**
     * 试验ID
     */
    private String experimentId;

    /**
     * 兵力名称
     */
    private String name;

    /**
     * 装备Id
     */
    private String equipmentId;

    /**
     * 模型编号
     */
    private String modelId;

    /**
     * 分类id
     */
    private String equipmentType;

    /**
     * 阵营
     */
    private String team;

    /**
     * 经度
     */
    private Object lon;

    /**
     * 纬度
     */
    private Object lat;

    /**
     * 海拔
     */
    private Object alt;

    /**
     * 航向角
     */
    private Object heading;

    /**
     * 速度
     */
    private Object speed;

    /**
     * 生命值
     */
    private Object life;

    /**
     * 模型属性
     */
    private String attributeInfo;

    /**
     * 输入参数
     */
    private String inputInfo;

    /**
     * 输出参数
     */
    private String outputInfo;

    /**
     * 搭载母体
     */
    private String parentId;

    @TableField(exist = false)
    private String equipmentTypeName;

    @TableField(exist = false)
    private List<ModelParameterBase> inputParameter;

    @TableField(exist = false)
    private List<ModelParameterBase> outputParameter;

    @TableField(exist = false)
    private String iconArmy;

    @TableField(exist = false)
    private String icon3dUrl;

    @TableField(exist = false)
    private List<ForcesCarry> relations;

    public String toArmyString() {
        return String.format("%s@%s@%s@%s@%s@%s@%s@%s@%s@%s@%s@%s@%s@%s@%s@%s#@@%s_%s_%s_%s_%s_%s_%s@",
                id, name, "", life == null ? "" : life, team, equipmentId, iconArmy, icon3dUrl, "0", "", "", "", "", "", equipmentId, parentId
                , lon == null ? "" : lon, lat == null ? "" : lat, alt == null ? "" : alt, heading == null ? "" : heading, "", "", speed == null ? "" : speed);
    }


}
