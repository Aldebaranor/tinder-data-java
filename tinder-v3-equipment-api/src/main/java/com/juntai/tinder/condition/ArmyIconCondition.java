package com.juntai.tinder.condition;

import com.juntai.soulboot.data.annotation.Eq;
import com.juntai.soulboot.data.annotation.Like;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/5/23
 */
@Data
public class ArmyIconCondition implements Serializable {

    @Eq(column = "id")
    private String id;

    @Eq(column = "type")
    private String type;

    @Like(column = "name")
    private String name;


}
