package com.juntai.tinder.facade;

import com.juntai.tinder.entity.ArmyIcon;

/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/5/23
 */
public interface ArmyIconFacade {

    /**
     * 主键查询
     *
     * @param id 主键
     * @return ArmyIcon
     */

    ArmyIcon getById(String id);



}
