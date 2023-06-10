package com.juntai.tinder.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * <p>
 * 装备名称
 * </p>
 *
 * @author nemo
 * @since 2023-05-07
 */
@Data
@TableName("meta_equipment_task")
public class EquipmentTask implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId
    private String id;

    /**
     * 装备编号
     */
    private String equipmentId;

    /**
     * 可执行任务json
     */
    private String tasks;

    /**
     * 装备名称
     */
    private String equipmentName;

    /**
     * 任务列表
     */
    @TableField(exist = false)
    private List<TaskType> taskList;

    @TableField(exist = false)
    private String taskNames;

}
