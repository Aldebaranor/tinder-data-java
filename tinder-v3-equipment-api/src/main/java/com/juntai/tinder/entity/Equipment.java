package com.juntai.tinder.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.juntai.tinder.entity.enums.CategoryType;
import com.juntai.tinder.entity.enums.Country;
import com.juntai.tinder.entity.enums.EquipmentKind;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 分类id
 * </p>
 *
 * @author nemo
 * @since 2023-05-07
 */
@Data
@TableName("meta_equipment")
public class Equipment implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId
    private String id;

    /**
     * 模型名称
     */
    private String name;

    /**
     * 同category的type
     */
    private CategoryType type;

    /**
     * 分类id
     */
    private String equipmentType;

    /**
     * 创建人部门
     */
    private String departmentId;

    /**
     * 描述
     */
    private String description;

    /**
     * 产地国家（字典）
     */
    private Country country;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 更新人
     */
    private String modifier;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime modifyTime;

    /**
     * 拼音首字母
     */
    private String pinyin;

    /**
     * 照片
     */
    private String photo;

    /**
     * 三维图标
     */
    private String icon3dUrl;

    /**
     * 军标
     */

    private String iconArmy;

    @TableField(exist = false)
    private EquipmentDetail detail;

    /**
     * 平台装配
     */
    @TableField(exist = false)
    private Map<EquipmentKind, List<EquipmentRelation>> relations;

    /**
     * 设备搭载
     */
    @TableField(exist = false)
    private List<EquipmentCarry> carries;

    /**
     * 武器搭载，数据从装配-武器系统-搭载获取出来
     */
    @TableField(exist = false)
    private List<EquipmentCarry> weaponCarries;

    /**
     * RCS 数据
     */
    @TableField(exist = false)
    private List<RcsData> rcs;

    /**
     * 国旗图片
     */
    @TableField(exist = false)
    private String countryFlag;

    @TableField(exist = false)
    private String departmentName;

    @TableField(exist = false)
    private String equipmentTypeName;


}
