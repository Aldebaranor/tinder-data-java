package com.juntai.tinder.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.juntai.soulboot.data.ConditionParser;
import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.tinder.condition.EquipmentRelationCondition;
import com.juntai.tinder.entity.EquipmentRelation;
import com.juntai.tinder.mapper.EquipmentRelationMapper;
import com.juntai.tinder.service.EquipmentRelationService;
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
public class EquipmentRelationServiceImpl  implements EquipmentRelationService {
    @Autowired
    private EquipmentRelationMapper mapper;
    @Override
    @Transactional(readOnly = true)
    public EquipmentRelation getById(String id) {
        return mapper.selectById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipmentRelation> list(EquipmentRelationCondition condition) {
        QueryChainWrapper<EquipmentRelation> wrapper = ChainWrappers.queryChain(EquipmentRelation.class);;
        ConditionParser.parse(wrapper, condition);
        return wrapper.list();
    }

    @Override
    @Transactional(readOnly = true)
    public Pagination<EquipmentRelation> page(Query<EquipmentRelationCondition, EquipmentRelation> query) {
        QueryChainWrapper<EquipmentRelation> wrapper = ChainWrappers.queryChain(EquipmentRelation.class);;
        ConditionParser.parse(wrapper, query.getCondition());
        return wrapper.page(query.toPage());
    }



    @Override
    public int update(EquipmentRelation entity) {
        return mapper.updateById(entity);
    }

    @Override
    public String insert(EquipmentRelation entity) {
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
    public void insertList(List<EquipmentRelation> list) {

    }
}
