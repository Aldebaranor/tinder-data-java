package com.juntai.tinder.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 装备模型库用来显示的
 * </p>
 *
 * @author nemo
 * @since 2023-05-07
 */
@Data
@TableName("meta_equipment_detail")
public class EquipmentDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId
    private String id;

    /**
     * 装备
     */
    private String equipmentId;

    /**
     * 基本信息
     */
    private String attributeInfo;

    /**
     * 装备模型库用来显示的
     */
    private String displayUrl;
}
