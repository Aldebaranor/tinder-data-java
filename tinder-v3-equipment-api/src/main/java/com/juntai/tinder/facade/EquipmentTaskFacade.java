package com.juntai.tinder.facade;

import com.juntai.tinder.entity.EquipmentTask;

import java.util.List;

/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/3/22
 */
public interface EquipmentTaskFacade {

    /**
     * 主键查询
     *
     * @param id 主键
     * @return OperateResult
     */

    EquipmentTask getById(String id);


    /**
     * 获取所有
     *
     * @return
     */
    List<EquipmentTask> getAll();

    /**
     * 包含任务的装备
     *
     * @param task
     * @return
     */

    List<String> containTask(String task);
}
