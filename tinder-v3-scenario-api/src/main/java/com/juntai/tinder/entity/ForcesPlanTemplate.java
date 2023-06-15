package com.juntai.tinder.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.juntai.tinder.model.ForcesPlanBaseModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author nemo
 * @since 2023-06-07
 */
@Getter
@Setter
@TableName("meta_forces_plan_template")
public class ForcesPlanTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private String id;

    /**
     * 试验ID
     */
    private String experimentId;

    /**
     * 计划名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 任务类型
     */
    private String taskType;

    /**
     * 计划动作
     */
    private String plan;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 1敌我属性 0 红方，1，蓝方，2，白方
     */
    private String team;

    @TableField(exist = false)
    private ForcesPlanBaseModel planInfo;
}
