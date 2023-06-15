package com.juntai.tinder.service;

import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.tinder.condition.EquipmentTypeCondition;
import com.juntai.tinder.entity.EquipmentType;
import com.juntai.tinder.facade.EquipmentTypeFacade;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author nemo
 * @since 2023-05-07
 */
public interface EquipmentTypeService extends EquipmentTypeFacade {


    /**
     * 列表查询
     *
     * @param condition
     * @return
     */
    List<EquipmentType> list(EquipmentTypeCondition condition);

    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    Pagination<EquipmentType> page(Query<EquipmentTypeCondition, EquipmentType> query);


    /**
     * 更新
     *
     * @param entity
     * @return
     */
    int update(EquipmentType entity);

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    String insert(EquipmentType entity);

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
     * 获取树
     *
     * @param parentId
     * @param hasEntity
     * @param type
     * @return
     */
    List<EquipmentType> tree(String parentId, Boolean hasEntity, String type);

    /**
     * 获取整棵树
     *
     * @param hasEntity
     * @param type
     * @return
     */
    List<EquipmentType> tree(Boolean hasEntity, String type);


}
