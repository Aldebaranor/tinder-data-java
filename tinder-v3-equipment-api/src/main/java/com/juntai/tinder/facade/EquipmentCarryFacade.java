package com.juntai.tinder.facade;

import com.juntai.tinder.entity.EquipmentCarry;

/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/5/23
 */
public interface EquipmentCarryFacade {

    /**
     * 主键查询
     *
     * @param id 主键
     * @return EquipmentRelation
     */

    EquipmentCarry getById(String id);


}
