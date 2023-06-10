package com.juntai.tinder.condition;

import com.juntai.soulboot.data.annotation.Eq;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/5/23
 */
@Data
public class EquipmentAssetCondition implements Serializable {


    @Eq(column = "id")
    private String id;

    @Eq(column = "equipment_id")
    private String equipmentId;

}