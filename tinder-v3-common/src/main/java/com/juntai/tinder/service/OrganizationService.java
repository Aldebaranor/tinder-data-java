package com.juntai.tinder.service;

import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.tinder.condition.OrganizationCondition;
import com.juntai.tinder.condition.UserCondition;
import com.juntai.tinder.entity.Organization;
import com.juntai.tinder.entity.User;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * Service
 *
 * @author 奔波儿灞
 * @version 1.0.0
 */
public interface OrganizationService {

    List<Organization> list(OrganizationCondition condition);

    Pagination<Organization> page(Query<OrganizationCondition, Organization> query);

    int insert(Organization entity);

    int update(Organization entity);

    @Cacheable(cacheNames = "soul:cache:organization",key = "'id:'+#p0")
    Organization getById(String id);
}
