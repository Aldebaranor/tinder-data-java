package com.juntai.tinder.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.juntai.soulboot.data.ConditionParser;
import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.soulboot.security.DefaultUserDetails;
import com.juntai.soulboot.security.UserDetails;
import com.juntai.soulboot.security.UserDetailsService;
import com.juntai.tinder.condition.OrganizationCondition;
import com.juntai.tinder.condition.UserCondition;
import com.juntai.tinder.entity.Organization;
import com.juntai.tinder.entity.User;
import com.juntai.tinder.mapper.OrganizationMapper;
import com.juntai.tinder.mapper.UserMapper;
import com.juntai.tinder.service.OrganizationService;
import com.juntai.tinder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Service Impl
 *
 * @author 奔波儿灞
 * @version 1.0.0
 */
@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private OrganizationMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<Organization> list(OrganizationCondition condition) {
        QueryChainWrapper<Organization> wrapper = ChainWrappers.queryChain(Organization.class);
        ConditionParser.parse(wrapper, condition);
        return wrapper.list();
    }

    @Override
    @Transactional(readOnly = true)
    public Pagination<Organization> page(Query<OrganizationCondition, Organization> query) {
        QueryChainWrapper<Organization> wrapper = ChainWrappers.queryChain(Organization.class);
        ConditionParser.parse(wrapper, query.getCondition());
        return wrapper.page(query.toPage());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(Organization entity) {
        return mapper.insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(Organization entity) {
        return mapper.updateById(entity);
    }

    @Override
    public Organization getById(String id) {
        return mapper.selectById(id);
    }


}
