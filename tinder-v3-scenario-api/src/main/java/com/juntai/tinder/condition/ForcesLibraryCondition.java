package com.juntai.tinder.condition;

import com.juntai.soulboot.data.annotation.Eq;
import com.juntai.soulboot.data.annotation.IsNull;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/3/22
 */
@Data
public class ForcesLibraryCondition implements Serializable {


    private static final long serialVersionUID = -5025566495463490681L;
    @Eq(column = "experiment_id")
    private String experimentId;

    @Eq(column = "id")
    private String id;

    @Eq(column = "team")
    private String team;

    @Eq(column = "name")
    private String name;

    @IsNull(column = "belong_id")
    private Boolean belongIdIsNull;

    @Eq(column = "belong_id")
    private String belongId;

}
