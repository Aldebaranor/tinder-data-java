package com.juntai.tinder.condition;

import com.juntai.soulboot.data.annotation.Eq;
import lombok.Data;

import java.io.Serializable;


/**
 * @author Administrator
 */
@Data

public class RcsDataCondition implements Serializable {

    @Eq(column = "id")
    private String id;

    @Eq(column = "group_id")
    private String groupId;

    @Eq(column = "type")
    private String type;

}
