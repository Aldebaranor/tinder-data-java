package com.juntai.tinder.controller;

import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.soulboot.web.api.ApiResultWrap;
import com.juntai.tinder.condition.OrganizationCondition;
import com.juntai.tinder.condition.UserCondition;
import com.juntai.tinder.entity.Organization;
import com.juntai.tinder.entity.User;
import com.juntai.tinder.service.OrganizationService;
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
@RequestMapping("/api/v1/organization")
public class OrganizationController {

    @Autowired
    private OrganizationService service;

    @PostMapping("/list")
    public List<Organization> list(OrganizationCondition condition) {
        return service.list(condition);
    }

    @PostMapping("/page")
    public Pagination<Organization> page(@RequestBody Query<OrganizationCondition, Organization> query) {
        return service.page(query);
    }

    @PostMapping
    public int insert(@RequestBody Organization organization) {
        return service.insert(organization);
    }

    @PutMapping
    public int update(@RequestBody Organization organization) {
        return service.update(organization);
    }

    @PostMapping("/delete-batch")
    public int deleteByIds(@RequestBody List<String> ids) {
        return service.deleteByIds(ids);
    }

    @DeleteMapping("/{id}")
    public int deleteById(@PathVariable String id) {
        return service.deleteById(id);
    }


    @GetMapping("/{id}")
    public Organization getById(@PathVariable String id) {
        return service.getById(id);
    }
}