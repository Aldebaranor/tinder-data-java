package com.juntai.tinder.service.impl;

import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.tinder.condition.ForcesPlanCondition;
import com.juntai.tinder.entity.ForcesPlan;
import com.juntai.tinder.mapper.ForcesPlanMapper;
import com.juntai.tinder.model.ForcesPlanModel;
import com.juntai.tinder.service.ForcesPlanService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
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

    private final ForcesPlanRepository forcesPlanRepository;

    private final ForcesRepository forcesRepository;

    @Override
    public ForcesPlan getById(String id) {
        ForcesPlan forcesPlan = super.getById(id);
        String plan = forcesPlan.getPlan();
        if (!StringUtils.isBlank(plan)) {
            forcesPlan.setPlanInfo(JsonUtils.deserialize(plan, ForcesPlanBaseModel.class));
        }
        return forcesPlan;
    }

    @Override
    public String insert(ForcesPlan entity) {
        Forces byId = forcesRepository.getById(entity.getForcesId());
        if (byId == null) {
            throw ExceptionUtils.api("没有该兵力，请确认兵力ID");
        }
        entity.setTeam(byId.getTeam());
        entity.setId(UUIDUtils.getHashUuid());
        if (entity.getPlanInfo() != null) {
            entity.setPlan(JsonUtils.serialize(entity.getPlanInfo()));
        }
        entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return super.insert(entity);
    }

    @Override
    public void insertList(List<ForcesPlan> entity) {
        if (entity.getPlanInfo() != null) {
            entity.setPlan(JsonUtils.serialize(entity.getPlanInfo()));
        }
        Forces byId = forcesRepository.getById(entity.getForcesId());
        if (byId == null) {
            throw ExceptionUtils.api("没有该兵力，请确认兵力ID");
        }
        entity.setTeam(byId.getTeam());
        super.update(entity);
    }

    @Override
    public void update(ForcesPlan entity) {

    }

    @Override
    public int deleteById(String id) {
        return 0;
    }

    @Override
    public int deleteByIds(List<String> ids) {
        return 0;
    }

    @Override
    public List<ForcesPlan> list(ForcesPlanCondition condition) {
        return super.query(condition, SortingBuilder.create().asc("createTime").toSorts());
    }

    @Override
    public Pagination<ForcesPlan> page(Query<ForcesPlanCondition, ForcesPlan> model) {
        if(model.getSorts() == null){
            model.setSorts(SortingBuilder.create().asc("createTime").toSorts());
        }
        return super.page(model.getCondition(), model.getPaging(), model.getSorts());
    }

    @Override
    public List<ForcesPlan> queryByExperiment(String experimentId, String forcesId, String team) {
        ForcesPlanCondition forcesPlanCondition = new ForcesPlanCondition();
        forcesPlanCondition.setExperimentId(experimentId);
        if (!StringUtils.isBlank(forcesId)) {
            forcesPlanCondition.setForcesId(forcesId);
        }
        if (!StringUtils.isBlank(team)) {
            forcesPlanCondition.setTeam(team);
        }
        List<ForcesPlan> list = super.query(forcesPlanCondition, SortingBuilder.create().asc("createTime").toSorts());
        for (ForcesPlan forcesPlan : list) {
            if (!StringUtils.isBlank(forcesPlan.getPlan())) {
                forcesPlan.setPlanInfo(JsonUtils.deserialize(forcesPlan.getPlan(), ForcesPlanBaseModel.class));
            }
        }
        return list;
    }

    @Override
    public List<ForcesPlanModel> getPlanModelExperiment(String experimentId) {
        List<ForcesPlanModel> list = new ArrayList<>();
        ForcesPlanCondition forcesPlanCondition = new ForcesPlanCondition();
        forcesPlanCondition.setExperimentId(experimentId);
        List<ForcesPlan> query = super.query(forcesPlanCondition, SortingBuilder.create().asc("createTime").toSorts());
        for (ForcesPlan plan : query) {
            ForcesPlanBaseModel base = JsonUtils.deserialize(plan.getPlan(), ForcesPlanBaseModel.class);
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
