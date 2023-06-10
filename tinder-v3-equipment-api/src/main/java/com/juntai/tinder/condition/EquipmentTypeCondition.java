package com.juntai.tinder.condition;

import com.juntai.soulboot.data.annotation.Eq;
import com.juntai.soulboot.data.annotation.Like;
import lombok.Data;

import java.io.Serializable;


/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/4/1
 */
@Data
public class EquipmentTypeCondition implements Serializable {


    @Eq(column = "id")
    private String id;

    @Eq(column = "code")
    private String code;

    @Like(column = "name")
    private String name;

    @Eq(column = "grade")
    private Integer grade;

    @Eq(column = "parent_id")
    private String parentId;

    @Eq(column = "disabled")
    private Boolean disabled;

}
