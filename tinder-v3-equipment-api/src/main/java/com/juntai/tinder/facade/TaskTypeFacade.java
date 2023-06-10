package com.juntai.tinder.facade;

import com.juntai.tinder.entity.TaskType;

import java.util.List;

/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/3/22
 */
public interface TaskTypeFacade {

    /**
     * 主键查询
     *
     * @param id 主键
     * @return OperateResult
     */

    TaskType getById(String id);


    /**
     * 获取所有
     * @return
     */
    List<TaskType> getAll();
}
