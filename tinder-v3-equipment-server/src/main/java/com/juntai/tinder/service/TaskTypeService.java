package com.juntai.tinder.service;

import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.tinder.condition.EquipmentCondition;
import com.juntai.tinder.condition.TaskTypeCondition;
import com.juntai.tinder.entity.Equipment;
import com.juntai.tinder.entity.TaskType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.juntai.tinder.facade.TaskTypeFacade;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author nemo
 * @since 2023-06-07
 */
public interface TaskTypeService  extends TaskTypeFacade {
    /**
     * 根据任务编号查询
     * @param code
     * @return
     */
    TaskType getByCode(String code);

    /**
     * 根据任务大类查询
     * @param type
     * @return
     */
    List<TaskType> getByType(String type);

    /**
     * 查询任务列表
     * @param condition
     * @return
     */
    List<TaskType> list(TaskTypeCondition condition);

    /**
     * 新增
     * @param entity
     * @return
     */
    String insert(TaskType entity);

    /**
     * 更新
     *
     * @param  entity
     */
    void update(TaskType entity);

    /**
     * 主键删除
     *
     * @param id 主键
     * @return 影响记录行数
     */

    int deleteById(String id);

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    int deleteByIds(List<String> ids);

    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    Pagination<TaskType> page(Query<TaskTypeCondition, TaskType> query);

}
