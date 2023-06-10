package com.juntai.tinder.condition;


import com.juntai.soulboot.data.annotation.Eq;
import com.juntai.soulboot.data.annotation.Like;
import com.juntai.tinder.entity.TaskType;
import com.juntai.tinder.entity.enums.TaskKind;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/3/22
 */
@Data
public class TaskTypeCondition implements Serializable {


    @Eq(column = "id")
    private String id;

    @Like(column = "name")
    private String name;

    @Eq(column = "code")
    private String code;

    @Eq(column = "type")
    private TaskKind type;

}
