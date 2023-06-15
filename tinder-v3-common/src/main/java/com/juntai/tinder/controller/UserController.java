package com.juntai.tinder.controller;

import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.soulboot.web.api.ApiResultWrap;
import com.juntai.tinder.condition.UserCondition;
import com.juntai.tinder.entity.User;
import com.juntai.tinder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller
 *
 * @author 奔波儿灞
 * @version 1.0.0
 */
@ApiResultWrap
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/list")
    public List<User> list(UserCondition condition) {
        return userService.list(condition);
    }

    @PostMapping("/page")
    public Pagination<User> page(@RequestBody Query<UserCondition, User> query) {
        return userService.page(query);
    }

    @PostMapping
    public User insert(@RequestBody User user) {
        return userService.insert(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        return userService.update(user);
    }
}