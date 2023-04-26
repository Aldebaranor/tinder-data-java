package com.juntai.tinder.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.juntai.tinder.condition.NoticeCondition;
import com.juntai.tinder.entity.Notice;
import com.juntai.tinder.mapper.NoticeMapper;
import com.juntai.tinder.service.NoticeService;
import com.juntai.soulboot.data.ConditionParser;
import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 系统提示 服务实现类
 * </p>
 *
 * @author nemo
 * @since 2023-04-09
 */
@Service
public class NoticeServiceImpl  implements NoticeService {

    @Autowired
    private NoticeMapper mapper;
    @Override
    @Transactional(readOnly = true)
    public Notice getById(String id) {
        return mapper.selectById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notice> list(NoticeCondition condition) {
        QueryChainWrapper<Notice> wrapper = ChainWrappers.queryChain(Notice.class);;
        ConditionParser.parse(wrapper, condition);
        return wrapper.list();
    }

    @Override
    @Transactional(readOnly = true)
    public Pagination<Notice> page(Query<NoticeCondition, Notice> query) {
        QueryChainWrapper<Notice> wrapper = ChainWrappers.queryChain(Notice.class);;
        ConditionParser.parse(wrapper, query.getCondition());
        return wrapper.page(query.toPage());
    }



    @Override
    public int update(Notice entity) {
        entity.setModifyTime(LocalDateTime.now());
        return mapper.updateById(entity);
    }

    @Override
    public String insert(Notice entity) {
        String id = UUID.randomUUID().toString();
        entity.setId(id);
        entity.setCreateTime(LocalDateTime.now());
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
