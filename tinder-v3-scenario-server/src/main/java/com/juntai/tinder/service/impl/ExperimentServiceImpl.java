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
import com.juntai.soulboot.security.UserContext;
import com.juntai.soulboot.util.JsonUtils;
import com.juntai.tinder.condition.ExperimentCondition;
import com.juntai.tinder.entity.*;
import com.juntai.tinder.entity.enums.TeamType;
import com.juntai.tinder.exception.TinderErrorCode;
import com.juntai.tinder.mapper.*;
import com.juntai.tinder.model.ForcesPlanBaseModel;
import com.juntai.tinder.service.ExperimentService;
import com.juntai.tinder.service.ExperimentTeamService;
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
import java.util.stream.Collectors;

/**
 * <p>
 * 试验类型 服务实现类
 * </p>
 *
 * @author nemo
 * @since 2023-06-07
 */
@Service
public class ExperimentServiceImpl implements ExperimentService {


    @Autowired
    private ExperimentMapper mapper;

    @Autowired
    private ExperimentTeamMapper experimentTeamMapper;

    @Autowired
    private ExperimentTeamService experimentTeamService;

    @Autowired
    private ForcesMapper forcesMapper;

    @Autowired
    private ForcesLibraryMapper forcesLibraryMapper;

    @Autowired
    private ForcesPlanMapper forcesPlanMapper;

    @Autowired
    private ForcesCarryMapper forcesCarryMapper;

    @Autowired
    private MapGeometryMapper mapGeometryMapper;

    @Autowired
    private MapPointMapper mapPointMapper;

    @Override
    public Experiment getById(String id) {
        Experiment experiment = mapper.selectById(id);
        if (experiment == null) {
            throw new SoulBootException(TinderErrorCode.TINDER_EXPERIMENT_ERROR, "没有该试验");
        }
        if (StringUtils.equals(UserContext.username(), experiment.getCreator())) {
            return experiment;
        }
        ExperimentTeam team = new LambdaQueryChainWrapper<>(experimentTeamMapper).eq(ExperimentTeam::getExperimentId, id)
                .eq(ExperimentTeam::getPersonId, UserContext.username()).last("limit 1").one();

        if (team == null) {
            throw new SoulBootException(TinderErrorCode.TINDER_EXPERIMENT_ERROR, "没有查看当前试验的权限");
        }
        if (StringUtils.equals(team.getTeam(), TeamType.BLUE.getValue())) {
            experiment.setIntelligenceBlue("******");
        } else if (StringUtils.equals(team.getTeam(), TeamType.RED.getValue())) {
            experiment.setIntelligenceRed("******");
        }
        return experiment;
    }

    @Override
    public Experiment getByIdNoAuth(String id) {
        Experiment experiment = mapper.selectById(id);
        if (experiment == null) {
            throw new SoulBootException(TinderErrorCode.TINDER_EXPERIMENT_ERROR, "没有该试验");
        }
        return experiment;
    }

    @Override
    public List<Experiment> all() {

        List<Experiment> experiments = new LambdaQueryChainWrapper<>(mapper).list();
        for (Experiment experiment : experiments) {
            long forces = new LambdaQueryChainWrapper<>(forcesMapper).eq(Forces::getExperimentId, experiment.getId()).count();
            experiment.setForcesCount(forces);
        }
        return experiments;
    }

    @Override
    public Experiment getByScenarioCode(String scenarioCode) {
        return new LambdaQueryChainWrapper<>(mapper).eq(Experiment::getScenarioCode, scenarioCode).last("limit 1").one();
    }

    @Override
    public String insert(Experiment experiment) {
        boolean exists = new LambdaQueryChainWrapper<>(mapper).eq(Experiment::getScenarioCode, experiment.getScenarioCode()).exists();

        if (exists) {
            throw new SoulBootException(TinderErrorCode.TINDER_EXPERIMENT_ERROR, "想定编号存在，请更想定编号");
        }
        String id = UUIDUtils.getHashUuid();
        experiment.setId(id);
        if (StringUtils.isEmpty(experiment.getCreator())) {
            experiment.setCreator(UserContext.username());
        }
        experiment.setCreateTime(LocalDateTime.now());
        experiment.setModifyTime(null);
        if (experiment.getType() == null) {
            experiment.setType(0);
        }
        mapper.insert(experiment);
        return id;
    }

    @Override
    public void update(Experiment experiment) {
        //需要判断修改权限
        String team = experimentTeamService.getTeam(experiment.getName());
        if (!StringUtils.equals(team, TeamType.WHITE.getValue())) {
            throw new SoulBootException(TinderErrorCode.TINDER_EXPERIMENT_ERROR, "没有修改当前试验的权限");
        }
        experiment.setModifier(UserContext.username());
        experiment.setModifyTime(LocalDateTime.now());
        mapper.updateById(experiment);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(String id) {
        Experiment experiment = mapper.selectById(id);
        if (experiment == null) {
            return 0;
        }
        if (!StringUtils.equals(UserContext.username(), experiment.getCreator())) {
            throw new SoulBootException(TinderErrorCode.TINDER_EXPERIMENT_ERROR, "没有删除当前试验的权限");
        }
        LambdaQueryWrapper<ExperimentTeam> wrapperExperimentTeam = new LambdaQueryWrapper<>();
        wrapperExperimentTeam.eq(ExperimentTeam::getExperimentId, id);
        experimentTeamMapper.delete(wrapperExperimentTeam);

        LambdaQueryWrapper<Forces> wrapperForces = new LambdaQueryWrapper<>();
        wrapperForces.eq(Forces::getExperimentId, id);
        forcesMapper.delete(wrapperForces);

        LambdaQueryWrapper<ForcesLibrary> wrapperForcesLibrary = new LambdaQueryWrapper<>();
        wrapperForcesLibrary.eq(ForcesLibrary::getExperimentId, id);
        forcesLibraryMapper.delete(wrapperForcesLibrary);

        LambdaQueryWrapper<ForcesPlan> wrapperForcesPlan = new LambdaQueryWrapper<>();
        wrapperForcesPlan.eq(ForcesPlan::getExperimentId, id);
        forcesPlanMapper.delete(wrapperForcesPlan);

        LambdaQueryWrapper<ForcesCarry> wrapperForcesCarry = new LambdaQueryWrapper<>();
        wrapperForcesCarry.eq(ForcesCarry::getExperimentId, id);
        forcesCarryMapper.delete(wrapperForcesCarry);

        LambdaQueryWrapper<MapGeometry> wrapperMapGeometry = new LambdaQueryWrapper<>();
        wrapperMapGeometry.eq(MapGeometry::getExperimentId, id);
        mapGeometryMapper.delete(wrapperMapGeometry);

        LambdaQueryWrapper<MapPoint> wrapperMapPoint = new LambdaQueryWrapper<>();
        wrapperMapPoint.eq(MapPoint::getExperimentId, id);
        mapPointMapper.delete(wrapperMapPoint);

        return mapper.deleteById(id);
    }

    @Override
    public Long count(ExperimentCondition condition) {
        QueryChainWrapper<Experiment> wrapper = ChainWrappers.queryChain(Experiment.class);
        ;
        ConditionParser.parse(wrapper, condition);
        return wrapper.count();
    }

    @Override
    public Pagination<Experiment> page(Query<ExperimentCondition, Experiment> query) {
        QueryChainWrapper<Experiment> wrapper = ChainWrappers.queryChain(Experiment.class);
        ;
        ConditionParser.parse(wrapper, query.getCondition());

        List<ExperimentTeam> list = new LambdaQueryChainWrapper<>(experimentTeamMapper)
                .eq(ExperimentTeam::getPersonId, UserContext.username()).list();
        List<String> ids = list.stream().map(ExperimentTeam::getExperimentId).collect(Collectors.toList());

        wrapper.and(i -> i.in("id", ids).or().eq("creator", UserContext.username()));
        if (CollectionUtils.isEmpty(query.getSorts())) {
            Sort sort = new Sort();
            sort.setColumn("create_time");
            sort.setOrder(Sort.Order.DESC);
            List<Sort> ts = new ArrayList<Sort>(Arrays.asList(sort));
            query.setSorts(ts);
        }
        return wrapper.page(query.toPage(Experiment.class));

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String copyById(String id, String name, String scenarioCode) {
        String head = String.valueOf(System.currentTimeMillis());
        //处理想定
        Experiment byId = this.getByIdNoAuth(id);
        if (StringUtils.isEmpty(name)) {
            byId.setName(byId.getName() + "_copy");
        } else {
            byId.setName(name);
        }
        if (StringUtils.isEmpty(scenarioCode)) {
            byId.setScenarioCode(byId.getScenarioCode() + "_copy");

        } else {
            byId.setScenarioCode(scenarioCode);
        }
        try {
            byId.valid();
        } catch (Exception ex) {
            throw new SoulBootException(TinderErrorCode.TINDER_EXPERIMENT_ERROR, ex.getMessage());
        }

        String newExperimentId = this.insert(byId);
        //处理MapPoint

        List<MapPoint> mapPoints = new LambdaQueryChainWrapper<>(mapPointMapper).eq(MapPoint::getExperimentId, id).list();
        if (!CollectionUtils.isEmpty(mapPoints)) {
            mapPoints.stream().forEach(q -> {
                q.setId(UUIDUtils.getHashUuid());
                q.setExperimentId(newExperimentId);
                mapPointMapper.insert(q);
            });
        }
        //处理 mapGeometry
        List<MapGeometry> mapGeometries = new LambdaQueryChainWrapper<>(mapGeometryMapper).eq(MapGeometry::getExperimentId, id).list();
        if (!CollectionUtils.isEmpty(mapGeometries)) {
            mapGeometries.stream().forEach(q -> {
                q.setId(UUIDUtils.getHashUuid());
                q.setExperimentId(newExperimentId);
                mapGeometryMapper.insert(q);
            });
        }
        //处理forces
        List<Forces> forces = new LambdaQueryChainWrapper<>(forcesMapper).eq(Forces::getExperimentId, id).list();
        if (!CollectionUtils.isEmpty(forces)) {
            forces.stream().forEach(q -> {
                q.setId(UUIDUtils.getHashUuid(head + q.getId()));
                q.setExperimentId(newExperimentId);
                forcesMapper.insert(q);
            });
        }
        //处理forcesPlan
        List<ForcesPlan> forcesPlans = new LambdaQueryChainWrapper<>(forcesPlanMapper).eq(ForcesPlan::getExperimentId, id).list();
        if (!CollectionUtils.isEmpty(forcesPlans)) {
            forcesPlans.stream().forEach(q -> {
                q.setId(UUIDUtils.getHashUuid());
                q.setForcesId(UUIDUtils.getHashUuid(head + q.getForcesId()));
                q.setExperimentId(newExperimentId);
                ForcesPlanBaseModel objects = JsonUtils.read(q.getPlan(), ForcesPlanBaseModel.class);
                if (objects != null && !CollectionUtils.isEmpty(objects.getTargets())) {
                    for (Long l : objects.getTargets()) {
                        String ls = UUIDUtils.getHashUuid(head + String.valueOf(l));
                        l = Long.valueOf(ls);
                    }
                }
                forcesPlanMapper.insert(q);
            });
        }
        //处理forcesCarry
        List<ForcesCarry> forcesCarries = new LambdaQueryChainWrapper<>(forcesCarryMapper).eq(ForcesCarry::getExperimentId, id).list();
        if (!CollectionUtils.isEmpty(forcesCarries)) {
            forcesCarries.stream().forEach(q -> {
                q.setId(UUIDUtils.getHashUuid());
                q.setBelongId(UUIDUtils.getHashUuid(head + q.getBelongId()));
                q.setExperimentId(newExperimentId);
                forcesCarryMapper.insert(q);
            });
        }
        //处理forcesCarry
        List<ForcesLibrary> forcesLibraries = new LambdaQueryChainWrapper<>(forcesLibraryMapper).eq(ForcesLibrary::getExperimentId, id).list();
        ;
        if (!CollectionUtils.isEmpty(forcesLibraries)) {
            forcesLibraries.stream().forEach(q -> {
                q.setId(UUIDUtils.getHashUuid());
                if (!StringUtils.isEmpty(q.getBelongId())) {
                    q.setBelongId(UUIDUtils.getHashUuid(head + q.getBelongId()));
                }
                q.setExperimentId(newExperimentId);
                forcesLibraryMapper.insert(q);
            });
        }

        return newExperimentId;
    }
}
