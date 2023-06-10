package com.juntai.tinder.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.juntai.soulboot.data.ConditionParser;
import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.tinder.condition.ModelCondition;
import com.juntai.tinder.entity.Model;
import com.juntai.tinder.mapper.ModelMapper;
import com.juntai.tinder.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 描述 服务实现类
 * </p>
 *
 * @author nemo
 * @since 2023-05-07
 */
@Service
public class ModelServiceImpl  implements ModelService {
    @Autowired
    private ModelMapper mapper;
    @Override
    @Transactional(readOnly = true)
    public Model getById(String id) {
        return mapper.selectById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Model> list(ModelCondition condition) {
        QueryChainWrapper<Model> wrapper = ChainWrappers.queryChain(Model.class);;
        ConditionParser.parse(wrapper, condition);
        return wrapper.list();
    }

    @Override
    @Transactional(readOnly = true)
    public Pagination<Model> page(Query<ModelCondition, Model> query) {
        QueryChainWrapper<Model> wrapper = ChainWrappers.queryChain(Model.class);;
        ConditionParser.parse(wrapper, query.getCondition());
        return wrapper.page(query.toPage());
    }



    @Override
    public int update(Model entity) {
        return mapper.updateById(entity);
    }

    @Override
    public Model seekById(String id) {
        return null;
    }

    @Override
    public List<Model> getAll() {
        return null;
    }

    @Override
    public Long count(ModelCondition condition) {
        return null;
    }


    @Override
    public List<Model> query(ModelCondition condition) {
        return null;
    }

    @Override
    public List<Model> getByEquipmentId(String equipmentId) {
        return null;
    }

    @Override
    public String insert(Model entity) {
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
    public void modify(String id, HashMap<String, Object> map) {

    }
}
