package com.juntai.tinder.service.impl;

import com.juntai.tinder.entity.Equipment;
import com.juntai.tinder.entity.Forces;
import com.juntai.tinder.entity.ForcesLibrary;
import com.juntai.tinder.mapper.ForcesMapper;
import com.juntai.tinder.model.Point;
import com.juntai.tinder.service.ForcesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * <p>
 * 搭载母体 服务实现类
 * </p>
 *
 * @author nemo
 * @since 2023-06-07
 */
@Service
public class ForcesServiceImpl  implements ForcesService {

    private final ForcesRepository forcesRepository;

    private final ModelFacade modelFacade;

    private final ForcesPlanRepository forcesPlanRepository;

    private final ForcesCarryRepository forcesCarryRepository;
    private final CommunicatesRepository communicatesRepository;
    private final CommunicatesPlanRepository communicatesPlanRepository;
    private final CommunicatesPlanLinkRepository communicatesPlanLinkRepository;

    private final EquipmentCache equipmentCache;
    @Override
    public String copyForces(String id, String experimentId, Point point) {
        Forces byId = super.seekById(id);
        if(byId == null){
            throw ExceptionUtils.api("没有该id的兵力");
        }
        if(point != null){
            byId.setLon(point.getLon()+0.00001);
            byId.setLat(point.getLat()+0.00001);
            byId.setAlt(point.getAlt());
        }
        if(StringUtils.isEmpty(experimentId)){
            byId.setName(byId.getName()+"-copy");
            experimentId = byId.getExperimentId();
        }
        String experimentIdNew = experimentId;
        String forcesId = this.insert(byId);
        //处理兵力的搭载
        List<ForcesCarry> relations = byId.getRelations();
        if(!CollectionUtils.isEmpty(relations)){
            relations.stream().forEach(q->{
                q.setId(UUIDUtils.getHashUuid());
                q.setBelongId(forcesId);
                q.setExperimentId(experimentIdNew);});
            forcesCarryRepository.insertList(relations);
        }
        //处理兵力的任务
        List<ForcesPlan> plans = forcesPlanRepository.query(SingleClause.equal("forcesId", id));
        if(!CollectionUtils.isEmpty(plans)){
            plans.stream().forEach(q->{
                q.setId(UUIDUtils.getHashUuid());
                q.setForcesId(forcesId);
                q.setExperimentId(experimentIdNew);});
            forcesPlanRepository.insertList(plans);
        }

        return forcesId;
    }

    @Override
    public String copyById(String id, String experimentId) {
        return null;
    }

    @Override
    public Forces getById(String id) {
        return this.copyForces(id,experimentId,null);
    }

    @Override
    public String insert(Forces entity) {
        entity.setId(UUIDUtils.getHashUuid());
        return super.insert(entity);
    }

    @Override
    public void insertList(List<Forces> entity) {

    }

    @Override
    public String addForces(String experimentId, String team, Equipment equipment, String modelId) {
        //判断是否有重名
        if (StringUtils.isBlank(equipment.getName())) {
            throw ExceptionUtils.api("名称不能为空");
        }
        Model model = modelFacade.seekById(modelId);
        if (model == null) {
            throw ExceptionUtils.api("模型数据为空");
        }
        ForcesCondition condition = new ForcesCondition();
        condition.setExperimentId(experimentId);

        List<Forces> query = super.query(condition);
        String name = equipment.getName();
        int suffixNum = 1;
        for (Forces forces : query) {
            String forceName = forces.getName();
            if(forces.getName().startsWith(name + "-")){
                String suffix = forceName.replace(name+"-","");
                try {
                    int num = Integer.parseInt(suffix);
                    if(num > suffixNum){
                        suffixNum = num + 1;
                    }
                } catch (NumberFormatException e) {
                    continue;
                }
            }
        }
        equipment.setName(name+ "-" +suffixNum);
        Forces forces = new Forces();
        forces.setTeam(team);
        forces.setExperimentId(experimentId);
        forces.setEquipmentType(equipment.getEquipmentType());
        forces.setName(equipment.getName());
        forces.setId(UUIDUtils.getHashUuid());
        if (equipment.getDetail() != null) {
            forces.setAttributeInfo(equipment.getDetail().getAttributeInfo());
        }
        forces.setModelId(model.getId());
        forces.setInputInfo(model.getInputInfo());
        forces.setOutputInfo(model.getOutputInfo());
        return super.insert(forces);
    }

    @Override
    public String addForcesFromLibrary(String experimentId, String team, ForcesLibrary equipment) {
        Forces forces = new Forces();
        forces.setTeam(team);
        forces.setExperimentId(experimentId);
        forces.setEquipmentType(forcesLibrary.getEquipmentType());
        forces.setEquipmentId(forcesLibrary.getEquipmentId());
        forces.setModelId(forcesLibrary.getModelId());
        forces.setName(forcesLibrary.getName());
        forces.setId(UUIDUtils.getHashUuid());
        forces.setAttributeInfo(forcesLibrary.getAttributeInfo());
        forces.setInputInfo(forcesLibrary.getInputInfo());
        forces.setOutputInfo(forcesLibrary.getOutputInfo());
        String forcesId = super.insert(forces);

        //关联新增搭载
        if (!CollectionUtils.isEmpty(forcesLibrary.getRelations())) {
            List<ForcesCarry> relations = toForcesLibrary(forcesId, forcesLibrary.getRelations());
            forcesCarryRepository.insertList(relations);
        }
        return forcesId;
    }

    @Override
    public void update(ForcesUpdateModel entity) {
        if (StringUtils.isBlank(entity.getId())) {
            throw ExceptionUtils.api("id 不能为空");
        }
        HashMap<String, Object> map = new LinkedHashMap<>(16);
        if (entity.getAlt() != null) {
            map.put("alt", entity.getAlt());
        }
        if (entity.getLon() != null) {
            map.put("lon", entity.getLon());
        }
        if (entity.getLat() != null) {
            map.put("lat", entity.getLat());
        }
        if (entity.getHeading() != null) {
            map.put("heading", entity.getHeading());
        }
        if (entity.getSpeed() != null) {
            map.put("speed", entity.getSpeed());
        }
        if (entity.getLife() != null) {
            map.put("life", entity.getLife());
        }
        if (!StringUtils.isBlank(entity.getName())) {
            map.put("name", entity.getName());
        }
        if (!StringUtils.isBlank(entity.getInputInfo())) {
            map.put("inputInfo", entity.getInputInfo());
        }
        ForcesCondition condition = new ForcesCondition();
        condition.setId(entity.getId());
        super.modify(map, condition);
    }

    @Override
    public void updateParentId(ForcesUpdateModel entity) {
        if (StringUtils.isBlank(entity.getId())) {
            throw ExceptionUtils.api("id 不能为空");
        }
        HashMap<String, Object> map = new LinkedHashMap<>(16);
        map.put("parentId", entity.getParentId());
        ForcesCondition condition = new ForcesCondition();
        condition.setId(entity.getId());
        super.modify(map, condition);
    }
    }

    @Override
    public int deleteById(String id) {
        forcesCarryRepository.deleteByForceId(id);
        forcesPlanRepository.deleteByForceId(id);
        communicatesRepository.deleteByForceId(id);
        communicatesPlanRepository.deleteByForceId(id);
        communicatesPlanLinkRepository.deleteByDestForceId(id);
        communicatesPlanLinkRepository.deleteBySourceForceId(id);
        return super.deleteById(id);
    }

    @Override
    public int deleteByIds(List<String> ids) {
        forcesCarryRepository.deleteByForceIds(ids);
        forcesPlanRepository.deleteByForceIds(ids);
        communicatesRepository.deleteByForceIds(ids);
        communicatesPlanLinkRepository.deleteByDestForceIds(ids);
        communicatesPlanLinkRepository.deleteBySourceForceIds(ids);
        return super.deleteByIds(ids);
    }

    @Override
    public List<Forces> list(ForcesCondition condition) {
        List<Forces> list = super.query(condition, SortingBuilder.create().asc("name").toSorts());
        for(Forces force :list){
            Equipment byTypeAndTeam = equipmentCache.getCacheData(force.getEquipmentId());
            if (org.apache.commons.lang3.StringUtils.isBlank(byTypeAndTeam.getIconArmy())) {
                force.setIconArmy("军标库_无人艇.svg");
            } else {
                force.setIconArmy(byTypeAndTeam.getIconArmy());
            }
            force.setIcon3dUrl(byTypeAndTeam.getIcon3dUrl());
        }
        return list;
    }

    @Override
    public List<Forces> seekByExperiment(String experimentId) {
        ForcesCondition condition = new ForcesCondition();
        condition.setExperimentId(experimentId);
        return super.seek(condition);
    }

    @Override
    public List<String> queryByExperiment(String experimentId, String team) {
        ForcesCondition condition = new ForcesCondition();
        condition.setExperimentId(experimentId);
        if (!StringUtils.isBlank(team)) {
            condition.setTeam(team);
        }
        List<Forces> query = super.query(condition, SortingBuilder.create().asc("name").toSorts());
        List<String> result = new ArrayList<>();
        for(Forces force :query){
            Equipment byTypeAndTeam = equipmentCache.getCacheData(force.getEquipmentId());
            if (org.apache.commons.lang3.StringUtils.isBlank(byTypeAndTeam.getIconArmy())) {
                force.setIconArmy("军标库_无人艇.svg");
            } else {
                force.setIconArmy(byTypeAndTeam.getIconArmy());
            }
            force.setIcon3dUrl(byTypeAndTeam.getIcon3dUrl());
            result.add(force.toArmyString());
        }
        return result;
    }

    @Override
    public void flashInput(String forcesId) {
        Forces byId = this.getById(forcesId);
        if(byId == null){
            return;
        }
        List<ModelParameter> oldParameters = JsonUtils.deserializeList(byId.getInputInfo(), ModelParameter.class);

        String input = modelFacade.getInput(byId.getModelId());

        List<ModelParameter> modelParameters = JsonUtils.deserializeList(input, ModelParameter.class);
        for (ModelParameter parameter : modelParameters) {
            ModelParameter temp = oldParameters.stream().filter(q -> StringUtils.equals(q.getName(), parameter.getName())).findFirst().orElse(null);
            if(temp == null){
                oldParameters.add(parameter);
            }
        }
        String newInput = JsonUtils.serialize(oldParameters);
        HashMap<String, Object> map = new LinkedHashMap<>(16);
        map.put("inputInfo", newInput);
        super.modify(map, SingleClause.equal("id", forcesId));
    }

    private List<ForcesCarry> toForcesLibrary(String belongId, List<ForcesLibrary> list) {
        List<ForcesCarry> relations = new ArrayList<>();
        for (ForcesLibrary library : list) {
            ForcesCarry relation = new ForcesCarry();
            relation.setId(UUIDUtils.getHashUuid());
            relation.setBelongId(belongId);
            relation.setName(library.getName());
            relation.setNum(library.getNum());
            relation.setExperimentId(library.getExperimentId());
            relation.setInputInfo(library.getInputInfo());
            relation.setOutputInfo(library.getOutputInfo());
            relation.setAttributeInfo(library.getAttributeInfo());
            relation.setModelId(library.getModelId());
            relation.setExperimentId(library.getExperimentId());
            relation.setEquipmentId(library.getEquipmentId());
            relation.setEquipmentType(library.getEquipmentType());
            relations.add(relation);
        }
        return relations;
    }
}
