package com.juntai.tinder.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.List;

import com.juntai.tinder.entity.enums.CategoryType;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author nemo
 * @since 2023-05-07
 */
@Data
@TableName("meta_equipment_type")
public class EquipmentType implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 等级
     */
    private Integer grade;

    /**
     * 上级ID
     */
    private String parentId;

    /**
     * 在同一级中的次序
     */
    private Integer sort;

    /**
     * 分类
     */
    private CategoryType type;

    @TableField(exist = false)
    private List<EquipmentType> children;

    @TableField(exist = false)
    private List<Equipment> equipments;
}
