package com.juntai.tinder.service;

import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.tinder.condition.EquipmentTaskCondition;
import com.juntai.tinder.entity.EquipmentTask;
import com.juntai.tinder.facade.EquipmentTaskFacade;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 装备名称 服务类
 * </p>
 *
 * @author nemo
 * @since 2023-05-07
 */
public interface EquipmentTaskService extends EquipmentTaskFacade {
    /**
     * 主键查询
     * @param id
     * @return
     */
    EquipmentTask getById(String id);

    /**
     * 列表查询
     * @param condition
     * @return
     */
    List<EquipmentTask> list(EquipmentTaskCondition condition);

    /**
     * 分页查询
     * @param query
     * @return
     */
    Pagination<EquipmentTask> page(Query<EquipmentTaskCondition, EquipmentTask> query);


    /**
     * 更新
     * @param entity
     * @return
     */
    int update(EquipmentTask entity) ;

    /**
     * 批量新增
     * @param list
     * @return
     */
    void insertList(List<EquipmentTask> list);

    /**
     * 新增
     * @param entity
     * @return
     */
    String insert(EquipmentTask entity) ;

    /**
     * 主键删除
     *
     * @param id 主键
     * @return 成功与否
     */

    int deleteById(String id);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    int deleteByIds(List<String> ids);

    EquipmentTask getByEquipmentId(String equipmentId);

    void modify(String id, HashMap<String, Object> map);
}
