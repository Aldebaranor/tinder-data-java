package com.juntai.tinder.condition;

import com.juntai.soulboot.data.annotation.Eq;
import lombok.Data;

import java.io.Serializable;


/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/6/1
 */
@Data

public class ModelRelationCondition implements Serializable {


    @Eq(column = "id")
    private String id;

    @Eq(column = "member_id")
    private String memberId;

    @Eq(column = "member_name")
    private String memberName;

    @Eq(column = "member_version")
    private String memberVersion;

    @Eq(column = "belong_id")
    private String belongId;

    @Eq(column = "belong_name")
    private String belongName;
}
