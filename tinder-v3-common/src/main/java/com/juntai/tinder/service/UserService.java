package com.juntai.tinder.service;

import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.tinder.condition.UserCondition;
import com.juntai.tinder.entity.User;

import java.util.List;

/**
 * Service
 *
 * @author 奔波儿灞
 * @version 1.0.0
 */
public interface UserService {

    List<User> list(UserCondition condition);

    Pagination<User> page(Query<UserCondition, User> query);

    User insert(User user);

    User update(User user);
}
