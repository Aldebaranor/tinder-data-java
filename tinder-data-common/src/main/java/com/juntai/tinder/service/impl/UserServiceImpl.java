package com.juntai.tinder.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.juntai.soulboot.data.ConditionParser;
import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.soulboot.security.DefaultUserDetails;
import com.juntai.soulboot.security.UserDetails;
import com.juntai.soulboot.security.UserDetailsService;
import com.juntai.tinder.condition.UserCondition;
import com.juntai.tinder.entity.User;
import com.juntai.tinder.mapper.UserMapper;
import com.juntai.tinder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Impl
 *
 * @author 奔波儿灞
 * @version 1.0.0
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public List<User> list(UserCondition condition) {
        QueryChainWrapper<User> wrapper = ChainWrappers.queryChain(User.class);
        ConditionParser.parse(wrapper, condition);
        return wrapper.list();
    }

    @Override
    @Transactional(readOnly = true)
    public User getById(String id) {

        return userMapper.selectById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Pagination<User> page(Query<UserCondition, User> query) {
        QueryChainWrapper<User> wrapper = ChainWrappers.queryChain(User.class);
        ConditionParser.parse(wrapper, query.getCondition());
        return wrapper.page(query.toPage(User.class));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User insert(User user) {
        userMapper.insert(user);
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User update(User user) {
        userMapper.updateById(user);
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        User u = ChainWrappers.lambdaQueryChain(User.class).eq(User::getName, username).last("limit 1").one();
        return u == null ? null : new DefaultUserDetails(u.getId(), username, u.getPassword());
    }


}
