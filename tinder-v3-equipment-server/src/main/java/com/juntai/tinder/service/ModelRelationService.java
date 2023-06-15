package com.juntai.tinder.service;

import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.tinder.condition.ModelRelationCondition;
import com.juntai.tinder.entity.ModelRelation;
import com.juntai.tinder.facade.ModelRelationFacade;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author nemo
 * @since 2023-05-07
 */
public interface ModelRelationService extends ModelRelationFacade {
    /**
     * 分页查询
     *
     * @param model QueryModel
     * @return 分页数据
     */
    Pagination<ModelRelation> page(Query<ModelRelationCondition, ModelRelation> model);

    ModelRelation getById(String id);

    List<ModelRelation> list(ModelRelationCondition condition);

    int deleteById(String id);


    int deleteByIds(List<String> ids);

    int update(ModelRelation entity);

    String insert(ModelRelation entity);
}
