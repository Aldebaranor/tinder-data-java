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

public class ForcesPlanCondition implements Serializable {

    private static final long serialVersionUID = -889978991596587608L;
    @Eq(column = "experiment_id")
    private String experimentId;

    @Eq(column = "forces_id")
    private String forcesId;

    @Eq(column = "team")
    private String team;


}
