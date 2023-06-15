package com.juntai.tinder.facade;

import com.juntai.tinder.entity.EquipmentRelation;

/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/5/23
 */
public interface EquipmentRelationFacade {

    /**
     * 主键查询
     *
     * @param id 主键
     * @return EquipmentRelation
     */

    EquipmentRelation getById(String id);


}
