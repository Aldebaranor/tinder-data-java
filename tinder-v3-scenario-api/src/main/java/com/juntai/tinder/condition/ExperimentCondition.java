package com.juntai.tinder.condition;

import com.juntai.soulboot.data.annotation.Eq;
import com.juntai.soulboot.data.annotation.Like;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/3/22
 */
@Data
public class ExperimentCondition implements Serializable {

    private static final long serialVersionUID = -9079185582276832593L;
    @Like(column = "name")
    private String name;

    @Eq(column = "scenario_code")
    private String scenarioCode;

    @Eq(column = "type")
    private String type;


}
