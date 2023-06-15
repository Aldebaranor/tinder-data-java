package com.juntai.tinder.facade;

import com.juntai.tinder.entity.EquipmentDetail;

/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/3/8
 */

public interface EquipmentDetailFacade {

    /**
     * 主键查询
     *
     * @param id 主键
     * @return EquipmentDetail
     */

    EquipmentDetail getById(String id);


}
