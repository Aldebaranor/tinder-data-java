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
 * 
 * </p>
 *
 * @author nemo
 * @since 2023-06-07
 */
@Getter
@Setter
@TableName("meta_forces_carry")
public class ForcesCarry implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private String id;

    /**
     * 试验ID
     */
    private String experimentId;

    /**
     * 宿主ID
     */
    private String belongId;

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
     * 数量
     */
    private Integer num;

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

    @TableField(exist = false)
    private String equipmentTypeName;

    @TableField(exist = false)
    private List<ModelParameterBase> inputParameter;

    @TableField(exist = false)
    private List<ModelParameterBase> outputParameter;
}
