package com.juntai.tinder.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

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
@TableName("meta_forces_library")
public class ForcesLibrary implements Serializable {

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
     * 装备编号
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
     * 宿主装备ID
     */
    private String belongId;

    /**
     * 数量
     */
    private Integer num;

    @TableField(exist = false)
    private String iconArmy;

    @TableField(exist = false)
    private String icon3dUrl;

    @TableField(exist = false)
    private List<ForcesLibrary> relations = new ArrayList<>();
}
