package com.juntai.tinder.condition;

import com.juntai.soulboot.data.annotation.Eq;
import com.juntai.soulboot.data.annotation.Gte;
import com.juntai.soulboot.data.annotation.Like;
import com.juntai.soulboot.data.annotation.Lte;
import com.juntai.tinder.entity.enums.UserType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 条件
 *
 * @author 奔波儿灞
 * @version 1.0.0
 */
@Data
public class OrganizationCondition {


    @Like(column = "id")
    private String id;

    @Like(column = "name")
    private String name;


}
