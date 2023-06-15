package com.juntai.tinder.service;

import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.tinder.condition.EquipmentCarryCondition;
import com.juntai.tinder.entity.EquipmentCarry;
import com.juntai.tinder.facade.EquipmentCarryFacade;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author nemo
 * @since 2023-05-07
 */
public interface EquipmentCarryService extends EquipmentCarryFacade {
    /**
     * 主键查询
     *
     * @param id
     * @return
     */
    EquipmentCarry getById(String id);

    /**
     * 列表查询
     *
     * @param condition
     * @return
     */
    List<EquipmentCarry> list(EquipmentCarryCondition condition);

    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    Pagination<EquipmentCarry> page(Query<EquipmentCarryCondition, EquipmentCarry> query);


    /**
     * 更新
     *
     * @param entity
     * @return
     */
    int update(EquipmentCarry entity);

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    String insert(EquipmentCarry entity);

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


    /**
     * 批量新增
     *
     * @param list
     * @return
     */
    void insertList(List<EquipmentCarry> list);


}
