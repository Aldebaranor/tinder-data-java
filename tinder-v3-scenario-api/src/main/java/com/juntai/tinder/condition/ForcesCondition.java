package com.juntai.tinder.condition;

import com.juntai.soulboot.data.annotation.Eq;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/3/22
 */
@Data

public class ForcesCondition implements Serializable {

    private static final long serialVersionUID = -6696132988977856732L;
    @Eq(column = "experiment_id")
    private String experimentId;

    @Eq(column = "id")
    private String id;

    @Eq(column = "team")
    private String team;

    @Eq(column = "name")
    private String name;

}
