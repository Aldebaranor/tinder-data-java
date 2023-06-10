package com.juntai.tinder.condition;

import com.juntai.soulboot.data.annotation.Eq;
import com.juntai.soulboot.data.annotation.In;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/3/22
 */
@Data
public class ForcesPlanTemplateCondition implements Serializable {

    private static final long serialVersionUID = 7467551060300925379L;

    @Eq(column = "experiment_id")
    private String experimentId;

    @Eq(column = "forces_id")
    private String forcesId;

    @Eq(column = "team")
    private String team;

    @In(column = "team")
    private List<String> teams;


}
