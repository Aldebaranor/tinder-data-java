package com.juntai.tinder.service.impl;

import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.tinder.condition.ExperimentCondition;
import com.juntai.tinder.entity.Experiment;
import com.juntai.tinder.mapper.*;
import com.juntai.tinder.service.ExperimentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * <p>
 * 试验类型 服务实现类
 * </p>
 *
 * @author nemo
 * @since 2023-06-07
 */
@Service
public class ExperimentServiceImpl  implements ExperimentService {


    @Autowired
    private  ExperimentMapper mapper;

    @Autowired
    private ExperimentTeamMapper experimentTeamMapper;

    @Autowired
    private ForcesMapper forcesMapper;

    @Autowired
    private ForcesLibraryMapper forcesLibraryMapper;

    @Autowired
    private ForcesPlanMapper forcesPlanMapper;

    @Autowired
    private  ForcesCarryMapper forcesCarryMapper;

    @Autowired
    private  MapGeometryMapper mapGeometryMapper;

    @Autowired
    private  MapPointMapper mapPointMapper;
    @Override
    public Experiment getById(String id) {
        Experiment experiment = super.getById(id);
        if (experiment == null) {
            throw ExceptionUtils.api("没有该试验");
        }
        if (StringUtils.equals(UserContext.username(), experiment.getCreator())) {
            return experiment;
        }
        String personId = UserContext.personId();
        if (StringUtils.isBlank(personId)) {
            throw ExceptionUtils.api("当前账号未绑定用户");
        }

        ExperimentTeam team = experimentTeamRepository.getByExperimentIdAndPersonId(id, personId).stream().findFirst().orElse(null);
        if (team == null) {
            throw ExceptionUtils.api("没有查看当前试验的权限");
        }
        if (StringUtils.equals(team.getTeam(), TeamType.BLUE.getText())) {
            experiment.setIntelligenceBlue("******");
        } else if (StringUtils.equals(team.getTeam(), TeamType.RED.getText())) {
            experiment.setIntelligenceRed("******");
        }
        return experiment;
    }

    @Override
    public Experiment getByIdNoAuth(String id) {
        Experiment experiment = super.getById(id);
        if (experiment == null) {
            throw ExceptionUtils.api("没有该试验");
        }
        return experiment;
    }

    @Override
    public List<Experiment> all() {
        List<Experiment> experiments = super.query(new ExperimentCondition());
        for (Experiment experiment : experiments) {
            long forces = forcesRepository.count(SingleClause.equal("experimentId", experiment.getId()));
            experiment.setForcesCount(forces);
        }
        return experiments;
    }

    @Override
    public Experiment getByScenarioCode(String scenarioCode) {
        ExperimentCondition condition = new ExperimentCondition();
        condition.setScenarioCode(scenarioCode);
        List<Experiment> query = super.query(condition);
        return query.stream().findFirst().orElse(null);
    }

    @Override
    public String insert(Experiment experiment) {
        ExperimentCondition condition = new ExperimentCondition();
        condition.setScenarioCode(experiment.getScenarioCode());
        Boolean exists = super.exists(condition);
        if(exists){
            throw ExceptionUtils.api("想定编号存在，请更想定编号");
        }
        experiment.setId(UUIDUtils.getHashUuid());
        if(StringUtils.isEmpty(experiment.getCreator())){
            experiment.setCreator(UserContext.username());
        }
        experiment.setCreateTime(new Timestamp(System.currentTimeMillis()));
        experiment.setModifyTime(null);
        if (experiment.getType() == null) {
            experiment.setType(0);
        }
        return super.insert(experiment);
    }

    @Override
    public void update(Experiment experiment) {
//需要判断修改权限
        Boolean flag = false;
        if (StringUtils.equals(UserContext.username(), experiment.getCreator())) {
            flag = true;
        } else {
            String personId = UserContext.personId();
            if (StringUtils.isBlank(personId)) {
                throw ExceptionUtils.api("当前账号未绑定用户");
            }
            ExperimentTeam team = experimentTeamRepository.getByExperimentIdAndPersonId(experiment.getId(), personId).stream().findFirst().orElse(null);
            if (team == null) {
                throw ExceptionUtils.api("没有修改当前试验的权限");
            }
            if (!StringUtils.equals(team.getTeam(), TeamType.WHITE.getValue())) {
                throw ExceptionUtils.api("没有修改当前试验的权限");
            }
            flag = true;
        }
        if (flag) {
            experiment.setModifier(UserContext.username());
            experiment.setModifyTime(new Timestamp(System.currentTimeMillis()));
            super.update(experiment);
        }
    }

    @Override
    public int deleteById(String id) {
        Experiment experiment = super.getById(id);
        if (experiment == null) {
            return 0;
        }
        if (!StringUtils.equals(UserContext.username(), experiment.getCreator())) {
            throw ExceptionUtils.api("没有删除当前试验的权限");
        }
        experimentTeamRepository.deleteByExperimentId(id);
        experimentTeamRepository.clearCache();
        forcesRepository.deleteByExperimentId(id);
        forcesRepository.clearCache();
        forcesLibraryRepository.deleteByExperimentId(id);
        forcesLibraryRepository.clearCache();
        forcesPlanRepository.deleteByExperimentId(id);
        forcesPlanRepository.clearCache();
        forcesCarryRepository.deleteByExperimentId(id);
        forcesCarryRepository.clearCache();
        mapGeometryRepository.deleteByExperimentId(id);
        mapGeometryRepository.clearCache();
        mapPointRepository.deleteByExperimentId(id);
        mapPointRepository.clearCache();
        communicatesRepository.deleteByExperimentId(id);
        communicatesRepository.clearCache();
        communicatesPlanRepository.deleteByExperimentId(id);
        communicatesPlanRepository.clearCache();
        communicatesPlanLinkRepository.deleteByExperimentId(id);
        communicatesPlanLinkRepository.clearCache();
        return super.deleteById(id);
    }

    @Override
    public Long count(ExperimentCondition condition) {
        return super.count(condition);
    }

    @Override
    public Pagination<Experiment> page(Query<ExperimentCondition, Experiment> query) {
        ChildClause childClause = ChildClause.include("id", "experimentId", "meta_experiment_team");
        childClause.add(SingleClause.equal("personId", UserContext.personId()));
        Clause clause = ClauseBuilder.and()
                .add(ConditionParser.parser(model.getCondition()))
                .add(
                        ClauseBuilder.or()
                                .add(SingleClause.equal("creator", UserContext.username()))
                                .add(childClause)
                                .toClause()
                ).toClause();
        Paging paging = Optional.ofNullable(model.getPaging())
                .orElse(new Paging());
        if(model.getSorts() == null){
            model.setSorts(SortingBuilder.create().asc("createTime").toSorts());
        }
        List<Experiment> experiments = experimentRepository.query(clause, model.getPaging(), model.getSorts());
        return PageResult.of(experiments, paging.getTotalCount());
    }

    @Override
    public int deleteByIds(List<String> ids) {
        for (String id : ids) {
            Experiment experiment = super.getById(id);
            if (experiment == null) {
                throw ExceptionUtils.api("存在无效的id");
            }
            if (!StringUtils.equals(UserContext.username(), experiment.getCreator())) {
                throw ExceptionUtils.api("存在没有删除当前试验的权限");
            }
        }
        experimentTeamRepository.deleteByExperimentIds(ids);
        experimentTeamRepository.clearCache();
        forcesRepository.deleteByExperimentIds(ids);
        forcesRepository.clearCache();
        forcesLibraryRepository.deleteByExperimentIds(ids);
        forcesLibraryRepository.clearCache();
        forcesPlanRepository.deleteByExperimentIds(ids);
        forcesPlanRepository.clearCache();
        forcesCarryRepository.deleteByExperimentIds(ids);
        forcesCarryRepository.clearCache();
        mapGeometryRepository.deleteByExperimentIds(ids);
        mapGeometryRepository.clearCache();
        mapPointRepository.deleteByExperimentIds(ids);
        mapPointRepository.clearCache();
        communicatesRepository.deleteByExperimentIds(ids);
        communicatesRepository.clearCache();

        return super.deleteByIds(ids);
    }

    @Override
    public String copyById(String id, String name, String scenarioCode) {
        String head = String.valueOf(System.currentTimeMillis());
        //处理想定
        Experiment byId = this.getByIdNoAuth(id);
        if(StringUtils.isEmpty(name)){
            byId.setName(byId.getName()+"_copy");
        }else{
            byId.setName(name);
        }
        if(StringUtils.isEmpty(scenarioCode)){
            byId.setScenarioCode(byId.getScenarioCode()+"_copy");

        }else{
            byId.setScenarioCode(scenarioCode);
        }
        try {
            byId.valid();
        }catch (Exception ex){
            throw ExceptionUtils.api(ex.getMessage());
        }

        String newExperimentId = this.insert(byId);
        //处理MapPoint
        List<MapPoint> mapPoints = mapPointRepository.query(SingleClause.equal("experimentId", id));
        if(!CollectionUtils.isEmpty(mapPoints)){
            mapPoints.stream().forEach(q->{
                q.setId(UUIDUtils.getHashUuid());
                q.setExperimentId(newExperimentId);});
            mapPointRepository.insertList(mapPoints);
        }
        //处理 mapGeometry
        List<MapGeometry> mapGeometries = mapGeometryRepository.query(SingleClause.equal("experimentId", id));
        if(!CollectionUtils.isEmpty(mapGeometries)){
            mapGeometries.stream().forEach(q->{
                q.setId(UUIDUtils.getHashUuid());
                q.setExperimentId(newExperimentId);});
            mapGeometryRepository.insertList(mapGeometries);
        }
        //处理forces
        List<Forces> forces = forcesRepository.query(SingleClause.equal("experimentId", id));
        if(!CollectionUtils.isEmpty(forces)){
            forces.stream().forEach(q->{
                q.setId(UUIDUtils.getHashUuid(head+q.getId()));
                q.setExperimentId(newExperimentId);});
            forcesRepository.insertList(forces);
        }
        //处理forcesPlan
        List<ForcesPlan> forcesPlans = forcesPlanRepository.query(SingleClause.equal("experimentId", id));
        if(!CollectionUtils.isEmpty(forcesPlans)){
            forcesPlans.stream().forEach(q->{
                q.setId(UUIDUtils.getHashUuid());
                q.setForcesId(UUIDUtils.getHashUuid(head+q.getForcesId()));
                q.setExperimentId(newExperimentId);
                ForcesPlanBaseModel objects = JsonUtils.deserialize(q.getPlan(), ForcesPlanBaseModel.class);
                if(objects !=null && ! CollectionUtils.isEmpty(objects.getTargets())){
                    for(Long l:objects.getTargets() ){
                        String ls = UUIDUtils.getHashUuid(head+String.valueOf(l));
                        l = Long.valueOf(ls);
                    }
                }
            });
            forcesPlanRepository.insertList(forcesPlans);
        }
        //处理forcesCarry
        List<ForcesCarry> forcesCarries = forcesCarryRepository.query(SingleClause.equal("experimentId", id));
        if(!CollectionUtils.isEmpty(forcesCarries)){
            forcesCarries.stream().forEach(q->{
                q.setId(UUIDUtils.getHashUuid());
                q.setBelongId(UUIDUtils.getHashUuid(head+q.getBelongId()));
                q.setExperimentId(newExperimentId);});
            forcesCarryRepository.insertList(forcesCarries);
        }
        //处理forcesCarry
        List<ForcesLibrary> forcesLibraries = forcesLibraryRepository.query(SingleClause.equal("experimentId", id));
        if(!CollectionUtils.isEmpty(forcesLibraries)){
            forcesLibraries.stream().forEach(q->{
                q.setId(UUIDUtils.getHashUuid());
                if(!StringUtils.isEmpty(q.getBelongId())){
                    q.setBelongId(UUIDUtils.getHashUuid(head+q.getBelongId()));
                }
                q.setExperimentId(newExperimentId);});
            forcesLibraryRepository.insertList(forcesLibraries);
        }
        //处理communication
        List<Communicates> communicates = communicatesRepository.query(SingleClause.equal("experimentId", id));
        if(!CollectionUtils.isEmpty(communicates)){
            communicates.stream().forEach(q->{
                q.setId(UUID.randomUUID().toString());
                q.setExperimentId(newExperimentId);
                q.setForcesId(UUIDUtils.getHashUuid(head+q.getForcesId()));
                List<CommunicatesLink> objects = JsonUtils.deserializeList(q.getLinks(), CommunicatesLink.class);
                objects.stream().forEach(p->{
                    p.setId(UUIDUtils.getHashUuid(head+p.getId()));
                });
                q.setLinks(JsonUtils.serialize(objects));
            });

            communicatesRepository.insertList(communicates);
        }
        return newExperimentId;
    }
}
