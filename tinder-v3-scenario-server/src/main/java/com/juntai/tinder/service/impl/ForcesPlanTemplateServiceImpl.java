package com.juntai.tinder.service.impl;

import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.tinder.condition.ForcesPlanTemplateCondition;
import com.juntai.tinder.entity.ForcesPlanTemplate;
import com.juntai.tinder.mapper.ForcesPlanTemplateMapper;
import com.juntai.tinder.service.ForcesPlanTemplateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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
public class ForcesPlanTemplateServiceImpl  implements ForcesPlanTemplateService {

    private final ForcesPlanTemplateRepository forcesPlanRepository;

    private final ForcesRepository forcesRepository;
    @Override
    public ForcesPlanTemplate getById(String id) {
        ForcesPlanTemplate forcesPlan = super.getById(id);
        String plan = forcesPlan.getPlan();
        if (!StringUtils.isBlank(plan)) {
            forcesPlan.setPlanInfo(JsonUtils.deserialize(plan, ForcesPlanBaseModel.class));
        }
        return forcesPlan;
    }

    @Override
    public String insert(ForcesPlanTemplate entity) {
        entity.setId(UUID.randomUUID().toString());
        if (entity.getPlanInfo() != null) {
            entity.setPlan(JsonUtils.serialize(entity.getPlanInfo()));
        }
        entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return super.insert(entity);
    }

    @Override
    public void insertList(List<ForcesPlanTemplate> entity) {

    }

    @Override
    public void update(ForcesPlanTemplate entity) {
        if (entity.getPlanInfo() != null) {
            entity.setPlan(JsonUtils.serialize(entity.getPlanInfo()));
        }
        super.update(entity);
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
    public List<ForcesPlanTemplate> list(ForcesPlanTemplateCondition condition) {
        return super.query(condition, SortingBuilder.create().asc("createTime").toSorts());
    }

    @Override
    public Pagination<ForcesPlanTemplate> page(Query<ForcesPlanTemplateCondition, ForcesPlanTemplate> model) {
        if(model.getSorts() == null){
            model.setSorts(SortingBuilder.create().asc("createTime").toSorts());
        }
        return super.page(model.getCondition(), model.getPaging(), model.getSorts());
    }

    @Override
    public List<ForcesPlanTemplate> queryByExperiment(String experimentId, String team) {
        ForcesPlanTemplateCondition forcesPlanCondition = new ForcesPlanTemplateCondition();
        forcesPlanCondition.setExperimentId(experimentId);

        if (!StringUtils.isBlank(team)) {
            if (StringUtils.equals(team, "2")) {
                forcesPlanCondition.setTeam(team);
            } else {
                forcesPlanCondition.setTeams(Arrays.asList("2", team));
            }
        }
        List<ForcesPlanTemplate> list = super.query(forcesPlanCondition, SortingBuilder.create().asc("createTime").toSorts());
        for (ForcesPlanTemplate forcesPlan : list) {
            if (!StringUtils.isBlank(forcesPlan.getPlan())) {
                forcesPlan.setPlanInfo(JsonUtils.deserialize(forcesPlan.getPlan(), ForcesPlanBaseModel.class));
            }
        }
        return list;
    }
}
