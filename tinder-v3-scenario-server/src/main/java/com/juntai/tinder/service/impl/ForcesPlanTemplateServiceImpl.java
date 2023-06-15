package com.juntai.tinder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.juntai.soulboot.data.ConditionParser;
import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.soulboot.data.Sort;
import com.juntai.soulboot.util.JsonUtils;
import com.juntai.tinder.condition.ForcesPlanTemplateCondition;
import com.juntai.tinder.entity.ForcesPlanTemplate;
import com.juntai.tinder.mapper.ForcesMapper;
import com.juntai.tinder.mapper.ForcesPlanTemplateMapper;
import com.juntai.tinder.model.ForcesPlanBaseModel;
import com.juntai.tinder.service.ForcesPlanTemplateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 描述 服务实现类
 * </p>
 *
 * @author nemo
 * @since 2023-06-07
 */
@Service
public class ForcesPlanTemplateServiceImpl implements ForcesPlanTemplateService {

    @Autowired
    private ForcesPlanTemplateMapper mapper;

    @Autowired
    private ForcesMapper forcesMapper;

    @Override
    public ForcesPlanTemplate getById(String id) {
        ForcesPlanTemplate forcesPlan = mapper.selectById(id);
        String plan = forcesPlan.getPlan();
        if (!StringUtils.isBlank(plan)) {
            forcesPlan.setPlanInfo(JsonUtils.read(plan, ForcesPlanBaseModel.class));
        }
        return forcesPlan;
    }

    @Override
    public String insert(ForcesPlanTemplate entity) {
        String id = UUID.randomUUID().toString();
        entity.setId(id);
        if (entity.getPlanInfo() != null) {
            entity.setPlan(JsonUtils.write(entity.getPlanInfo()));
        }
        entity.setCreateTime(LocalDateTime.now());
        mapper.insert(entity);
        return id;
    }

    @Override
    public void insertList(List<ForcesPlanTemplate> entity) {
        entity.forEach(q -> {
            q.setId(UUID.randomUUID().toString());
            if (q.getPlanInfo() != null) {
                q.setPlan(JsonUtils.write(q.getPlanInfo()));
            }
            q.setCreateTime(LocalDateTime.now());
            mapper.insert(q);
        });
    }

    @Override
    public void update(ForcesPlanTemplate entity) {
        if (entity.getPlanInfo() != null) {
            entity.setPlan(JsonUtils.write(entity.getPlanInfo()));
        }
        mapper.updateById(entity);
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
    public List<ForcesPlanTemplate> list(ForcesPlanTemplateCondition condition) {
        QueryChainWrapper<ForcesPlanTemplate> wrapper = ChainWrappers.queryChain(ForcesPlanTemplate.class);
        ;
        ConditionParser.parse(wrapper, condition);
        return wrapper.orderByDesc("create_time").list();
    }

    @Override
    public Pagination<ForcesPlanTemplate> page(Query<ForcesPlanTemplateCondition, ForcesPlanTemplate> query) {
        if (CollectionUtils.isEmpty(query.getSorts())) {
            Sort sort = new Sort();
            sort.setColumn("create_time");
            sort.setOrder(Sort.Order.DESC);
            List<Sort> ts = new ArrayList<Sort>(Arrays.asList(sort));
            query.setSorts(ts);
        }
        QueryChainWrapper<ForcesPlanTemplate> wrapper = ChainWrappers.queryChain(ForcesPlanTemplate.class);
        ;
        ConditionParser.parse(wrapper, query.getCondition());
        return wrapper.page(query.toPage(ForcesPlanTemplate.class));
    }

    @Override
    public List<ForcesPlanTemplate> queryByExperiment(String experimentId, String team) {
        LambdaQueryWrapper<ForcesPlanTemplate> wrap = new LambdaQueryWrapper<>();
        wrap.eq(ForcesPlanTemplate::getExperimentId, experimentId);
        if (!StringUtils.isBlank(team)) {
            if (StringUtils.equals(team, "2")) {
                wrap.eq(ForcesPlanTemplate::getTeam, team);
            } else {
                wrap.in(ForcesPlanTemplate::getTeam, Arrays.asList("2", team));
            }
        }
        List<ForcesPlanTemplate> list = mapper.selectList(wrap.orderByAsc(ForcesPlanTemplate::getCreateTime));
        for (ForcesPlanTemplate forcesPlan : list) {
            if (!StringUtils.isBlank(forcesPlan.getPlan())) {
                forcesPlan.setPlanInfo(JsonUtils.read(forcesPlan.getPlan(), ForcesPlanBaseModel.class));
            }
        }
        return list;
    }
}
