package com.juntai.tinder.service.impl;

import com.juntai.tinder.condition.ForcesLibraryCondition;
import com.juntai.tinder.entity.ForcesLibrary;
import com.juntai.tinder.mapper.ForcesLibraryMapper;
import com.juntai.tinder.model.ForcesLibraryUpdateModel;
import com.juntai.tinder.service.ForcesLibraryService;
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
 *  服务实现类
 * </p>
 *
 * @author nemo
 * @since 2023-06-07
 */
@Service
public class ForcesLibraryServiceImpl  implements ForcesLibraryService {
    private final ForcesLibraryRepository forcesLibraryRepository;

    private final ForcesRepository forcesRepository;

    private final ModelFacade modelFacade;

    private final EquipmentFacade equipmentFacade;

    private final EquipmentCache equipmentCache;

    @Override
    public ForcesLibrary getById(String id) {
        return null;
    }

    @Override
    public ForcesLibrary getNewId(String id, String experimentId) {
        List<Forces> query = forcesRepository.query(SingleClause.equal("experimentId", experimentId));
        ForcesLibrary byId = this.getById(id);

        String name = byId.getName();
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
        byId.setName(name+ "-" +suffixNum);
        return byId;
    }

    @Override
    public ForcesLibrary seekById(String id) {
        return null;
    }

    @Override
    public String insert(ForcesLibrary entity) {
        entity.setId(UUIDUtils.getHashUuid());
        return super.insert(entity);
    }

    @Override
    public void insertList(List<ForcesLibrary> entity) {
        entities.stream().forEach(q->{
            q.setId(UUIDUtils.getHashUuid());});
        super.insertList(entities);
    }

    @Override
    public String copyForces(String id, String experimentId) {
        ForcesLibrary byId = super.getById(id);
        if(byId == null){
            throw ExceptionUtils.api("没有该id的兵力");
        }
        if(StringUtils.isEmpty(experimentId)){
            byId.setName(byId.getName()+"_copy");
            experimentId = byId.getExperimentId();
        }
        String experimentIdNew = experimentId;
        String forcesId = this.insert(byId);
        //处理兵力的搭载
        List<ForcesLibrary> relations = byId.getRelations();
        if(!CollectionUtils.isEmpty(relations)){
            relations.stream().forEach(q->{
                q.setId(UUIDUtils.getHashUuid());
                q.setBelongId(forcesId);
                q.setExperimentId(experimentIdNew);});
            this.insertList(relations);
        }
        return forcesId;
    }

    @Override
    public String addForces(String experimentId, String team, String equipmentId, String modelId) {
        Equipment equipment = equipmentFacade.seekById(equipmentId);
        if(equipment == null){
            throw ExceptionUtils.api("没有相关的装备信息");
        }
        Model model = modelFacade.seekById(modelId);
        if (model == null) {
            throw ExceptionUtils.api("模型数据为空");
        }
        return addNewForces(experimentId,team,equipment,model);
    }

    @Override
    public String addRelation(String experimentId, String team, String equipmentId, String modelId, String belongId, int num) {
        Equipment equipment = equipmentFacade.seekById(equipmentId);
        if(equipment == null){
            throw ExceptionUtils.api("没有相关的装备信息");
        }
        Model model = modelFacade.seekById(modelId);
        if (model == null) {
            throw ExceptionUtils.api("模型数据为空");
        }
        return addNewRelation(experimentId,team,equipment,model,belongId,num);
    }

    @Override
    public int deleteById(String id) {
        super.delete(SingleClause.equal("belongId",id));
        return super.deleteById(id);
    }

    @Override
    public int deleteByIds(List<String> ids) {
        ForcesLibrary byId = super.getById(id);
        if(byId == null){
            return null;
        }
        Equipment byTypeAndTeam = equipmentCache.getCacheData(byId.getEquipmentId());
        if (org.apache.commons.lang3.StringUtils.isBlank(byTypeAndTeam.getIconArmy())) {
            byId.setIconArmy("军标库_无人艇.svg");
        } else {
            byId.setIconArmy(byTypeAndTeam.getIconArmy());
        }
        byId.setIcon3dUrl(byTypeAndTeam.getIcon3dUrl());
        ForcesLibraryCondition condition = new ForcesLibraryCondition();
        condition.setExperimentId(byId.getExperimentId());
        condition.setBelongId(byId.getId());
        List<ForcesLibrary> query = this.query(condition);
        byId.setRelations(query);
        return byId;
    }

    @Override
    public List<ForcesLibrary> list(ForcesLibraryCondition condition) {
        List<ForcesLibrary> list = super.query(condition, SortingBuilder.create().asc("name").toSorts());
        for(ForcesLibrary library : list){
            Equipment byTypeAndTeam = equipmentCache.getCacheData(library.getEquipmentId());
            if (org.apache.commons.lang3.StringUtils.isBlank(byTypeAndTeam.getIconArmy())) {
                library.setIconArmy("军标库_无人艇.svg");
            } else {
                library.setIconArmy(byTypeAndTeam.getIconArmy());
            }
            library.setIcon3dUrl(byTypeAndTeam.getIcon3dUrl());
        }
        return list;
    }

    @Override
    public List<ForcesLibrary> queryByExperiment(String experimentId, String team) {
        if (StringUtils.isBlank(experimentId)) {
            throw ExceptionUtils.api("试验id不能为空");
        }
        ForcesLibraryCondition condition = new ForcesLibraryCondition();
        condition.setExperimentId(experimentId);
        if (!StringUtils.isBlank(team)) {
            condition.setTeam(team);
        }
        List<ForcesLibrary> query = super.query(condition, SortingBuilder.create().asc("name").toSorts());
        for(ForcesLibrary library : query){
            Equipment byTypeAndTeam = equipmentCache.getCacheData(library.getEquipmentId());
            if (org.apache.commons.lang3.StringUtils.isBlank(byTypeAndTeam.getIconArmy())) {
                library.setIconArmy("军标库_无人艇.svg");
            } else {
                library.setIconArmy(byTypeAndTeam.getIconArmy());
            }
            library.setIcon3dUrl(byTypeAndTeam.getIcon3dUrl());
        }
        return buildTree(query);
    }

    @Override
    public void update(ForcesLibraryUpdateModel entity) {
        if (StringUtils.isBlank(entity.getId())) {
            throw ExceptionUtils.api("id 不能为空");
        }
        HashMap<String, Object> map = new LinkedHashMap<>(16);
        if (!StringUtils.isBlank(entity.getName())) {
            map.put("name", entity.getName());
        }
        if (!StringUtils.isBlank(entity.getInputInfo())) {
            map.put("inputInfo", entity.getInputInfo());
        }
        if (entity.getNum() != null) {
            map.put("num", entity.getNum());
        }
        ForcesLibraryCondition condition = new ForcesLibraryCondition();
        condition.setId(entity.getId());
        super.modify(map, condition);
    }

    @Override
    public void updateCarry(String belongId, ForcesLibraryUpdateModel entity) {
        if (StringUtils.isBlank(entity.getId())) {
            throw ExceptionUtils.api("id 不能为空");
        }
        if (StringUtils.isBlank(belongId)) {
            throw ExceptionUtils.api("belongId 不能为空");
        }
        ForcesLibraryCondition condition = new ForcesLibraryCondition();
        condition.setId(entity.getId());
        condition.setBelongId(belongId);
        Boolean exists = super.exists(condition);
        if (!exists) {
            throw ExceptionUtils.api("没有该ID的兵力搭载");
        }
        HashMap<String, Object> map = new LinkedHashMap<>(16);
        if (!StringUtils.isBlank(entity.getName())) {
            map.put("name", entity.getName());
        }
        if (!StringUtils.isBlank(entity.getInputInfo())) {
            map.put("inputInfo", entity.getInputInfo());
        }
        if (entity.getNum() != null) {
            map.put("num", entity.getNum());
        }
        super.modify(map, condition);
    }

    @Override
    public void updateCarryList(String belongId, List<ForcesLibraryUpdateModel> list) {
        if (StringUtils.isBlank(belongId)) {
            throw ExceptionUtils.api("belongId 不能为空");
        }
        for(ForcesLibraryUpdateModel model :list){
            if (StringUtils.isBlank(model.getId())) {
                continue;
            }
            ForcesLibraryCondition condition = new ForcesLibraryCondition();
            condition.setId(model.getId());
            condition.setBelongId(belongId);
            Boolean exists = super.exists(condition);
            if (!exists) {
                continue;
            }
            HashMap<String, Object> map = new LinkedHashMap<>(16);
            if (!StringUtils.isBlank(model.getName())) {
                map.put("name", model.getName());
            }
            if (!StringUtils.isBlank(model.getInputInfo())) {
                map.put("inputInfo", model.getInputInfo());
            }
            if (model.getNum() != null) {
                map.put("num", model.getNum());
            }
            super.modify(map, condition);
        }
    }

    @Override
    public int deleteCarryById(String belongId, String id) {
        if (StringUtils.isBlank(id)) {
            throw ExceptionUtils.api("id 不能为空");
        }
        if (StringUtils.isBlank(belongId)) {
            throw ExceptionUtils.api("belongId 不能为空");
        }
        ForcesLibraryCondition condition = new ForcesLibraryCondition();
        condition.setId(id);
        condition.setBelongId(belongId);
        Boolean exists = super.exists(condition);
        if (!exists) {
            throw ExceptionUtils.api("没有该ID的兵力搭载");
        }
        return super.delete(condition);
    }

    @Override
    public void updateName(String id, String newName) {
        HashMap<String, Object> map = new LinkedHashMap<>(16);
        map.put("name", newName);
        ForcesCondition condition = new ForcesCondition();
        condition.setId(id);
        super.modify(map, condition);
    }

    @Override
    public void flashInput(String forcesId) {

        ForcesLibrary byId = this.getById(forcesId);
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

    public static void setChildren(List<ForcesLibrary> list, ForcesLibrary parent) {
        for (ForcesLibrary forces : list) {
            if (parent.getId().equals(forces.getBelongId())) {
                parent.getRelations().add(forces);
            }
        }
        if (parent.getRelations().isEmpty()) {
            return;
        }
        for (ForcesLibrary forces : parent.getRelations()) {
            setChildren(list, forces);
        }
    }

    public String addNewForces(String experimentId, String team, Equipment equipment, Model model) {

        //判断是否有重名
        if (StringUtils.isBlank(equipment.getName())) {
            throw ExceptionUtils.api("名称不能为空");
        }
        ForcesLibrary forces = new ForcesLibrary();
        forces.setTeam(team);
        forces.setExperimentId(experimentId);
        forces.setEquipmentType(equipment.getEquipmentType());
        forces.setEquipmentId(equipment.getId());

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

    public String addNewRelation(String experimentId, String team, Equipment equipment, Model model, String belongId, int num) {
        //判断是否有重名
        if (StringUtils.isBlank(equipment.getName())) {
            throw ExceptionUtils.api("名称不能为空");
        }
        ForcesLibrary forces = new ForcesLibrary();
        forces.setTeam(team);
        forces.setExperimentId(experimentId);
        forces.setEquipmentType(equipment.getEquipmentType());
        forces.setEquipmentId(equipment.getId());
        forces.setName(equipment.getName());
        forces.setId(UUIDUtils.getHashUuid());
        if (equipment.getDetail() != null) {
            forces.setAttributeInfo(equipment.getDetail().getAttributeInfo());
        }
        forces.setModelId(model.getId());
        forces.setInputInfo(model.getInputInfo());
        forces.setOutputInfo(model.getOutputInfo());
        forces.setBelongId(belongId);
        forces.setNum(num);
        return super.insert(forces);

    }

    private List<ForcesLibrary> buildTree(List<ForcesLibrary> nodeList) {
        List<ForcesLibrary> result = new ArrayList<>();
        for (ForcesLibrary forces : nodeList) {
            if (StringUtils.isBlank(forces.getBelongId())) {
                setChildren(nodeList, forces);
                result.add(forces);
            }
        }
        return result;
    }
}
