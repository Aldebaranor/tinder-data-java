package com.juntai.tinder.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author nemo
 * @since 2023-05-07
 */
@Data
@TableName("meta_equipment_carry")
public class EquipmentCarry implements Serializable {

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
    private String kind;

    /**
     * 装配平台Id
     */
    private String belongId;
}
