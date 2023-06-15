package com.juntai.tinder.facade;

import com.juntai.tinder.entity.Equipment;

import java.util.List;

/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/5/23
 */
public interface EquipmentFacade {


    /**
     * 主键查询
     *
     * @param id 主键
     * @return OperateResult
     */

    Equipment seekById(String id);

    Equipment getById(String id);


    /**
     * 获取全部
     *
     * @param
     * @return java.util.List<Equipment>
     * @author huangkang
     * @desc
     * @date 2022/4/30 23:43
     */
    List<Equipment> getAll();


    /**
     * 获取模型
     *
     * @param equipmentType
     * @return java.util.List<Equipment>
     * @author huangkang
     * @desc
     * @date 2022/5/3 17:42
     */
    List<Equipment> getModelsByType(String equipmentType);

    Equipment seek(Equipment equipment);

}
