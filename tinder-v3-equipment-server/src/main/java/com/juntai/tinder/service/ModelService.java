package com.juntai.tinder.service;

import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.tinder.condition.ModelCondition;
import com.juntai.tinder.entity.Model;
import com.juntai.tinder.facade.ModelFacade;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 描述 服务类
 * </p>
 *
 * @author nemo
 * @since 2023-05-07
 */
public interface ModelService extends ModelFacade {
    /**
     * 分页查询
     *
     * @param model QueryModel
     * @return 分页数据
     */
    Pagination<Model> page(Query<ModelCondition, Model> model);


    /**
     * 查询装备下所有模型
     *
     * @param equipmentId
     * @return
     */
    List<Model> getByEquipmentId(String equipmentId);

    /**
     * 保存
     *
     * @param entity
     * @return OperateResult
     */

    String insert(Model entity);

    /**
     * 更新
     *
     * @param entity
     * @return OperateResult
     */
    int update(Model entity);

    /**
     * 主键删除
     *
     * @param id 主键
     * @return OperateResult
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
     * 某几个字段的修改
     *
     * @param id
     * @param map
     * @return void
     * @author huangkang
     * @desc
     * @date 2022/4/30 22:23
     */
    void modify(String id, HashMap<String, Object> map);
}
