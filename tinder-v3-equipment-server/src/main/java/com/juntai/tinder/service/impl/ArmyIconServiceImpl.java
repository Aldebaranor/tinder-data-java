package com.juntai.tinder.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.juntai.soulboot.data.ConditionParser;
import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.tinder.condition.ArmyIconCondition;
import com.juntai.tinder.entity.ArmyIcon;
import com.juntai.tinder.mapper.ArmyIconMapper;
import com.juntai.tinder.service.ArmyIconService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author nemo
 * @since 2023-05-07
 */
@Service
public class ArmyIconServiceImpl implements ArmyIconService {
    @Autowired
    private ArmyIconMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public ArmyIcon getById(String id) {
        return mapper.selectById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArmyIcon> list(ArmyIconCondition condition) {
        QueryChainWrapper<ArmyIcon> wrapper = ChainWrappers.queryChain(ArmyIcon.class);
        ;
        ConditionParser.parse(wrapper, condition);
        return wrapper.list();
    }

    @Override
    @Transactional(readOnly = true)
    public Pagination<ArmyIcon> page(Query<ArmyIconCondition, ArmyIcon> query) {
        QueryChainWrapper<ArmyIcon> wrapper = ChainWrappers.queryChain(ArmyIcon.class);
        ;
        ConditionParser.parse(wrapper, query.getCondition());
        return wrapper.page(query.toPage(ArmyIcon.class));
    }


    @Override
    public int update(ArmyIcon entity) {
        return mapper.updateById(entity);
    }

    @Override
    public String insert(ArmyIcon entity) {
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
}
