package com.juntai.tinder.service;

import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.tinder.condition.EquipmentRelationCondition;
import com.juntai.tinder.entity.EquipmentRelation;
import com.juntai.tinder.facade.EquipmentRelationFacade;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author nemo
 * @since 2023-05-07
 */
public interface EquipmentRelationService extends EquipmentRelationFacade {
    /**
     * 主键查询
     *
     * @param id
     * @return
     */
    EquipmentRelation getById(String id);

    /**
     * 列表查询
     *
     * @param condition
     * @return
     */
    List<EquipmentRelation> list(EquipmentRelationCondition condition);

    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    Pagination<EquipmentRelation> page(Query<EquipmentRelationCondition, EquipmentRelation> query);


    /**
     * 更新
     *
     * @param entity
     * @return
     */
    int update(EquipmentRelation entity);

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    String insert(EquipmentRelation entity);

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
    void insertList(List<EquipmentRelation> list);


}
