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

public class MapSettingCondition implements Serializable {

    private static final long serialVersionUID = -1116900161502340782L;
    @Eq(column = "scenario_code")
    private String scenarioCode;

    @Eq(column = "id")
    private String id;

    @Eq(column = "page_code")
    private String pageCode;

    @Eq(column = "disabled")
    private Boolean disabled;
}
