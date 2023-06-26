package com.juntai.tinder.condition;

import com.juntai.soulboot.data.annotation.Like;
import lombok.Data;

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
