package com.juntai.tinder.condition;


import com.juntai.soulboot.data.annotation.Eq;
import com.juntai.soulboot.data.annotation.Like;
import com.juntai.tinder.entity.enums.CategoryType;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/5/23
 */
@Data
public class EquipmentCondition implements Serializable {



    @Eq(column = "id")
    private String id;

    @Like(column = "name")
    private String name;


    @Eq(column = "type")
    private CategoryType type;

    @Like(column = "equipment_type")
    private String equipmentType;


    @Eq(column = "department_id")
    private String departmentId;

    @Like(column = "description")
    private String description;


    @Eq(column = "pinyin")
    private String pinyin;


    @Eq(column = "creator")
    private String creator;


    @Eq(column = "modifier")
    private String modifier;


}
