package com.juntai.tinder.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.juntai.tinder.entity.enums.EquipmentKind;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author nemo
 * @since 2023-05-07
 */
@Data
@TableName("meta_equipment_relation")
public class EquipmentRelation implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId
    private String id;

    /**
     * 平台分类
     */
    private String equipmentType;

    /**
     * 被装配设备主键
     */
    private String equipmentId;

    /**
     * 设备分类
     */
    private EquipmentKind kind;

    /**
     * 装配平台Id
     */
    private String belongId;
}
