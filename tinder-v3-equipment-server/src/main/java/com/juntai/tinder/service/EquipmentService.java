package com.juntai.tinder.service;

import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.tinder.condition.EquipmentCondition;
import com.juntai.tinder.entity.Equipment;
import com.juntai.tinder.facade.EquipmentFacade;

import java.util.List;

/**
 * <p>
 * 分类id 服务类
 * </p>
 *
 * @author nemo
 * @since 2023-05-07
 */
public interface EquipmentService extends EquipmentFacade {
    /**
     * 主键查询
     *
     * @param id
     * @return
     */
    Equipment getById(String id);

    /**
     * 列表查询
     *
     * @param condition
     * @return
     */
    List<Equipment> list(EquipmentCondition condition);

    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    Pagination<Equipment> page(Query<EquipmentCondition, Equipment> query);


    /**
     * 更新
     *
     * @param entity
     * @return
     */
    int update(Equipment entity);

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    String insert(Equipment entity);

    /**
     * 主键删除
     *
     * @param id 主键
     * @return 成功与否
     */

    int deleteById(String id);

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    int deleteByIds(List<String> ids);


}
