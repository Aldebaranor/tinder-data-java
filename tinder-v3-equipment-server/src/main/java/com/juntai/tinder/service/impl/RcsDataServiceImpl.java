package com.juntai.tinder.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.juntai.soulboot.data.ConditionParser;
import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.tinder.condition.RcsDataCondition;
import com.juntai.tinder.entity.RcsData;
import com.juntai.tinder.mapper.RcsDataMapper;
import com.juntai.tinder.service.RcsDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author nemo
 * @since 2023-06-07
 */
@Service
public class RcsDataServiceImpl implements RcsDataService {

    @Autowired
    private RcsDataMapper mapper;

    @Override
    public RcsData getById(String id) {
        return mapper.selectById(id);
    }

    @Override
    public String insert(RcsData entity) {
        String id = UUID.randomUUID().toString();
        entity.setId(id);
        mapper.insert(entity);
        return id;
    }

    @Override
    public void insertList(List<RcsData> list) {

    }

    @Override
    public int update(RcsData entity) {
        return mapper.updateById(entity);
    }

    @Override
    public int deleteById(String id) {
        return mapper.deleteById(id);
    }

    @Override
    public Pagination<RcsData> page(Query<RcsDataCondition, RcsData> query) {
        QueryChainWrapper<RcsData> wrapper = ChainWrappers.queryChain(RcsData.class);
        ;
        ConditionParser.parse(wrapper, query.getCondition());
        return wrapper.page(query.toPage(RcsData.class));
    }

    @Override
    public int deleteByIds(List<String> ids) {
        return mapper.deleteBatchIds(ids);
    }

    @Override
    public List<RcsData> list(RcsDataCondition condition) {
        QueryChainWrapper<RcsData> wrapper = ChainWrappers.queryChain(RcsData.class);
        ;
        ConditionParser.parse(wrapper, condition);
        return wrapper.list();
    }
}
