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

public class ModelCondition implements Serializable {


    @Eq(column = "id")
    private String id;


    @Eq(column = "equipment_id")
    private String equipmentId;


    @Eq(column = "file_id")
    private String fileId;


    @Eq(column = "modifier")
    private String modifier;


    @Eq(column = "version")
    private String version;


    @Eq(column = "creator")
    private String creator;

}
