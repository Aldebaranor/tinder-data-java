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
public class ExperimentTeamCondition implements Serializable {

    private static final long serialVersionUID = -5921149392487501131L;

    @Eq(column = "experiment_id")
    private String experimentId;

    @Eq(column = "person_id")
    private String personId;

}
