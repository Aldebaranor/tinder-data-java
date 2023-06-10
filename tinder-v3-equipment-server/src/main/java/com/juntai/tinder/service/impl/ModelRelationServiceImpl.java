package com.juntai.tinder.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.juntai.soulboot.data.ConditionParser;
import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.tinder.condition.ModelRelationCondition;
import com.juntai.tinder.entity.ModelRelation;
import com.juntai.tinder.mapper.ModelRelationMapper;
import com.juntai.tinder.service.ModelRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author nemo
 * @since 2023-05-07
 */
@Service
public class ModelRelationServiceImpl  implements ModelRelationService {
    @Autowired
    private ModelRelationMapper mapper;
    @Override
    @Transactional(readOnly = true)
    public ModelRelation getById(String id) {
        return mapper.selectById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ModelRelation> list(ModelRelationCondition condition) {
        QueryChainWrapper<ModelRelation> wrapper = ChainWrappers.queryChain(ModelRelation.class);;
        ConditionParser.parse(wrapper, condition);
        return wrapper.list();
    }

    @Override
    @Transactional(readOnly = true)
    public Pagination<ModelRelation> page(Query<ModelRelationCondition, ModelRelation> query) {
        QueryChainWrapper<ModelRelation> wrapper = ChainWrappers.queryChain(ModelRelation.class);;
        ConditionParser.parse(wrapper, query.getCondition());
        return wrapper.page(query.toPage());
    }



    @Override
    public int update(ModelRelation entity) {
        return mapper.updateById(entity);
    }

    @Override
    public String insert(ModelRelation entity) {
        String id = UUID.randomUUID().toString();
        entity.setId(id);
        mapper.insert(entity);
        return id;
    }


    @Override
    public int deleteById(String id) {
        return mapper.deleteById(id);
    }

    @Override
    public int deleteByIds(List<String> ids) {
        return mapper.deleteBatchIds(ids);
    }

    @Override
    public ModelRelation seekById(String id) {
        return null;
    }

    @Override
    public List<ModelRelation> modelId(String modelId) {
        return null;
    }
}
