package com.juntai.tinder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.juntai.soulboot.common.exception.SoulBootException;
import com.juntai.soulboot.data.ConditionParser;
import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.soulboot.data.Sort;
import com.juntai.soulboot.util.JsonUtils;
import com.juntai.tinder.condition.ForcesPlanCondition;
import com.juntai.tinder.entity.Forces;
import com.juntai.tinder.entity.ForcesPlan;
import com.juntai.tinder.exception.TinderErrorCode;
import com.juntai.tinder.mapper.ForcesMapper;
import com.juntai.tinder.mapper.ForcesPlanMapper;
import com.juntai.tinder.model.ForcesPlanBaseModel;
import com.juntai.tinder.model.ForcesPlanModel;
import com.juntai.tinder.service.ForcesPlanService;
import com.juntai.tinder.utils.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * <p>
 * 1敌我属性 0 红方，1，蓝方，2，白方 服务实现类
 * </p>
 *
 * @author nemo
 * @since 2023-06-07
 */
@Service
public class ForcesPlanServiceImpl implements ForcesPlanService {

    @Autowired
    private ForcesPlanMapper mapper;

    @Autowired
    private ForcesMapper forcesMapper;

    @Override
    public ForcesPlan getById(String id) {
        ForcesPlan forcesPlan = mapper.selectById(id);
        String plan = forcesPlan.getPlan();
        if (!StringUtils.isBlank(plan)) {
            forcesPlan.setPlanInfo(JsonUtils.read(plan, ForcesPlanBaseModel.class));
        }
        return forcesPlan;
    }

    @Override
    public String insert(ForcesPlan entity) {
        Forces byId = forcesMapper.selectById(entity.getForcesId());
        if (byId == null) {
            throw new SoulBootException(TinderErrorCode.TINDER_FORCES_PLAN_ERROR, "没有该兵力，请确认兵力ID");
        }
        String id = UUIDUtils.getHashUuid();
        entity.setTeam(byId.getTeam());
        entity.setId(id);
        if (entity.getPlanInfo() != null) {
            entity.setPlan(JsonUtils.write(entity.getPlanInfo()));
        }
        entity.setCreateTime(LocalDateTime.now());
        mapper.insert(entity);
        return id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertList(List<ForcesPlan> entity) {
        entity.forEach(q -> {
            Forces byId = forcesMapper.selectById(q.getForcesId());
            if (byId == null) {
                throw new SoulBootException(TinderErrorCode.TINDER_FORCES_PLAN_ERROR, "没有该兵力，请确认兵力ID");
            }
            String id = UUIDUtils.getHashUuid();
            q.setTeam(byId.getTeam());
            q.setId(id);
            if (q.getPlanInfo() != null) {
                q.setPlan(JsonUtils.write(q.getPlanInfo()));
            }
            q.setCreateTime(LocalDateTime.now());
            mapper.insert(q);
        });
    }

    @Override
    public void update(ForcesPlan entity) {
        Forces byId = forcesMapper.selectById(entity.getForcesId());
        if (byId == null) {
            throw new SoulBootException(TinderErrorCode.TINDER_FORCES_PLAN_ERROR, "没有该兵力，请确认兵力ID");
        }
        if (entity.getPlanInfo() != null) {
            entity.setPlan(JsonUtils.write(entity.getPlanInfo()));
        }
        entity.setTeam(byId.getTeam());
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
    public List<ForcesPlan> list(ForcesPlanCondition condition) {
        QueryChainWrapper<ForcesPlan> wrapper = ChainWrappers.queryChain(ForcesPlan.class);
        ;
        ConditionParser.parse(wrapper, condition);
        return wrapper.orderByDesc("create_time").list();
    }

    @Override
    public Pagination<ForcesPlan> page(Query<ForcesPlanCondition, ForcesPlan> query) {
        if (CollectionUtils.isEmpty(query.getSorts())) {
            Sort sort = new Sort();
            sort.setColumn("create_time");
            sort.setOrder(Sort.Order.DESC);
            List<Sort> ts = new ArrayList<Sort>(Arrays.asList(sort));
            query.setSorts(ts);
        }
        QueryChainWrapper<ForcesPlan> wrapper = ChainWrappers.queryChain(ForcesPlan.class);
        ;
        ConditionParser.parse(wrapper, query.getCondition());
        return wrapper.page(query.toPage(ForcesPlan.class));
    }

    @Override
    public List<ForcesPlan> queryByExperiment(String experimentId, String forcesId, String team) {
        LambdaQueryWrapper<ForcesPlan> wrap = new LambdaQueryWrapper<>();
        wrap.eq(ForcesPlan::getExperimentId, experimentId);
        if (!StringUtils.isBlank(forcesId)) {
            wrap.eq(ForcesPlan::getForcesId, forcesId);
        }
        if (!StringUtils.isBlank(team)) {
            wrap.eq(ForcesPlan::getTeam, team);
        }
        List<ForcesPlan> list = mapper.selectList(wrap.orderByAsc(ForcesPlan::getCreateTime));
        for (ForcesPlan forcesPlan : list) {
            if (!StringUtils.isBlank(forcesPlan.getPlan())) {
                forcesPlan.setPlanInfo(JsonUtils.read(forcesPlan.getPlan(), ForcesPlanBaseModel.class));
            }
        }
        return list;
    }

    @Override
    public List<ForcesPlanModel> getPlanModelExperiment(String experimentId) {
        List<ForcesPlanModel> list = new ArrayList<>();
        List<ForcesPlan> query = new LambdaQueryChainWrapper<>(mapper).eq(ForcesPlan::getExperimentId, experimentId)
                .orderByAsc(ForcesPlan::getCreateTime).list();
        for (ForcesPlan plan : query) {
            ForcesPlanBaseModel base = JsonUtils.read(plan.getPlan(), ForcesPlanBaseModel.class);
            ForcesPlanModel model = new ForcesPlanModel();
            model.setForcesId(Long.valueOf(plan.getForcesId()));
            model.setId(Long.valueOf(plan.getId()));
            model.setName(plan.getName());
            model.setTaskType(plan.getTaskType());
            model.setStartTime(plan.getStartTime());
            model.setEndTime(plan.getEndTime());
            model.setTargets(base.getTargets());
            model.setExecutors(base.getExecutors());
            model.setPointsId(base.getPointsId());
            model.setPoints(base.getPoints());
            model.setAttributes(base.getAttributes());
            list.add(model);
        }
        return list;
    }
}
