package com.juntai.tinder.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.juntai.tinder.entity.enums.UserType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 实体
 *
 * @author 奔波儿灞
 * @version 1.0.0
 */
@Data
@TableName("t_user")
public class User {

    private Long id;

    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String username;

    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String password;

    private String name;

    private Integer age;

    private String email;

    private UserType type;

    private Long orgId;

    @TableField(updateStrategy = FieldStrategy.NEVER)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createAt;

    /**
     * 不是数据库字段
     */
    @TableField(exist = false)
    private String orgName;

}
