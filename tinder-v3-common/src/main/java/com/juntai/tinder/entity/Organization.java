package com.juntai.tinder.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 实体
 *
 * @author 奔波儿灞
 * @version 1.0.0
 */
@Data
@TableName("t_organization")
public class Organization {

    @TableId
    private String id;

    private String name;

    private String parentId;

}
