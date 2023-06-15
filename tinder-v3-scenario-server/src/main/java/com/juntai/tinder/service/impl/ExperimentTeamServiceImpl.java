package com.juntai.tinder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.juntai.soulboot.common.exception.SoulBootException;
import com.juntai.soulboot.data.ConditionParser;
import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.soulboot.security.UserContext;
import com.juntai.tinder.condition.ExperimentTeamCondition;
import com.juntai.tinder.entity.Experiment;
import com.juntai.tinder.entity.ExperimentTeam;
import com.juntai.tinder.entity.enums.TeamType;
import com.juntai.tinder.exception.TinderErrorCode;
import com.juntai.tinder.mapper.ExperimentMapper;
import com.juntai.tinder.mapper.ExperimentTeamMapper;
import com.juntai.tinder.service.ExperimentTeamService;
import com.juntai.tinder.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author nemo
 * @since 2023-06-07
 */
@Service
public class ExperimentTeamServiceImpl implements ExperimentTeamService {

    @Autowired
    public ExperimentTeamMapper mapper;

    @Autowired
    public ExperimentMapper experimentMapper;

    @Autowired
    public UserService userService;

    @Override
    public ExperimentTeam getById(String id) {
        return mapper.selectById(id);
    }

    @Override
    public String insert(ExperimentTeam experimentTeam) {
        String id = UUID.randomUUID().toString();
        experimentTeam.setId(id);
        experimentTeam.setCreator(UserContext.username());
        experimentTeam.setCreateTime(LocalDateTime.now());
        mapper.insert(experimentTeam);
        return id;
    }

    @Override
    public void insertList(List<ExperimentTeam> list) {
        for (ExperimentTeam experimentTeam : list) {
            experimentTeam.setId(UUID.randomUUID().toString());
            experimentTeam.setCreator(UserContext.username());
            experimentTeam.setCreateTime(LocalDateTime.now());
            mapper.insert(experimentTeam);
        }
    }

    @Override
    public void update(ExperimentTeam experimentTeam) {
        mapper.updateById(experimentTeam);
    }

    @Override
    public void updateListWithExperimentId(String experimentId, List<ExperimentTeam> list) {
        LambdaQueryWrapper<ExperimentTeam> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExperimentTeam::getExperimentId, experimentId);
        mapper.delete(wrapper);
        list.forEach(q -> {
            q.setId(UUID.randomUUID().toString());
            q.setCreator(UserContext.username());
            q.setCreateTime(LocalDateTime.now());
            mapper.insert(q);
        });

    }

    @Override
    public int deleteById(String id) {
        return mapper.deleteById(id);
    }

    @Override
    public Pagination<ExperimentTeam> page(Query<ExperimentTeamCondition, ExperimentTeam> query) {
        QueryChainWrapper<ExperimentTeam> wrapper = ChainWrappers.queryChain(ExperimentTeam.class);
        ;
        ConditionParser.parse(wrapper, query.getCondition());
        return wrapper.page(query.toPage(ExperimentTeam.class));
    }

    @Override
    public int deleteByIds(List<String> ids) {
        return 0;
    }

    @Override
    public List<ExperimentTeam> list(ExperimentTeamCondition condition) {

        QueryChainWrapper<ExperimentTeam> wrapper = ChainWrappers.queryChain(ExperimentTeam.class);
        ;
        ConditionParser.parse(wrapper, condition);
        List<ExperimentTeam> list = wrapper.list();
        list.forEach(q -> {
            q.setPerson(userService.getById(q.getPersonId()));
        });
        return list;
    }

    @Override
    public boolean checkAuthorization(String experimentId) {
        Experiment experiment = experimentMapper.selectById(experimentId);
        if (experiment == null) {
            throw new SoulBootException(TinderErrorCode.TINDER_EXPERIMENT_ERROR, "试验不存在");
        }
        // 试验创建者
        if (UserContext.username().equals(experiment.getCreator())) {
            return true;
        }
        return new LambdaQueryChainWrapper<>(mapper).eq(ExperimentTeam::getExperimentId, experimentId)
                .eq(ExperimentTeam::getPersonId, UserContext.username()).exists();

    }

    @Override
    public String getTeam(String experimentId) {
        Experiment experiment = experimentMapper.selectById(experimentId);
        if (experiment == null) {
            throw new SoulBootException(TinderErrorCode.TINDER_EXPERIMENT_ERROR, "试验不存在");
        }
        // 试验创建者
        if (UserContext.username().equals(experiment.getCreator())) {
            return TeamType.WHITE.getValue();
        }
        List<ExperimentTeam> query = new LambdaQueryChainWrapper<>(mapper).eq(ExperimentTeam::getExperimentId, experimentId)
                .eq(ExperimentTeam::getPersonId, UserContext.username()).list();
        if (CollectionUtils.isEmpty(query)) {
            throw new SoulBootException(TinderErrorCode.TINDER_EXPERIMENT_ERROR, "当前账号没有访问权限");
        }
        ExperimentTeam experimentTeam = query.stream().filter(q -> StringUtils.equals(q.getTeam(), TeamType.WHITE.getValue())).findFirst().orElse(null);
        if (experimentTeam != null) {
            return TeamType.WHITE.getValue();
        } else {
            return query.get(0).getTeam();
        }
    }
}
