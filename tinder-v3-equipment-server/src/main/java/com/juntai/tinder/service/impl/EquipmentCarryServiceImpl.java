package com.juntai.tinder.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.juntai.soulboot.data.ConditionParser;
import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.tinder.condition.EquipmentCarryCondition;
import com.juntai.tinder.entity.EquipmentCarry;
import com.juntai.tinder.mapper.EquipmentCarryMapper;
import com.juntai.tinder.service.EquipmentCarryService;
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
public class EquipmentCarryServiceImpl  implements EquipmentCarryService {
    @Autowired
    private EquipmentCarryMapper mapper;
    @Override
    @Transactional(readOnly = true)
    public EquipmentCarry getById(String id) {
        return mapper.selectById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipmentCarry> list(EquipmentCarryCondition condition) {
        QueryChainWrapper<EquipmentCarry> wrapper = ChainWrappers.queryChain(EquipmentCarry.class);;
        ConditionParser.parse(wrapper, condition);
        return wrapper.list();
    }

    @Override
    @Transactional(readOnly = true)
    public Pagination<EquipmentCarry> page(Query<EquipmentCarryCondition, EquipmentCarry> query) {
        QueryChainWrapper<EquipmentCarry> wrapper = ChainWrappers.queryChain(EquipmentCarry.class);;
        ConditionParser.parse(wrapper, query.getCondition());
        return wrapper.page(query.toPage());
    }



    @Override
    public int update(EquipmentCarry entity) {
        return mapper.updateById(entity);
    }

    @Override
    public String insert(EquipmentCarry entity) {
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
    public void insertList(List<EquipmentCarry> list) {

    }
}
