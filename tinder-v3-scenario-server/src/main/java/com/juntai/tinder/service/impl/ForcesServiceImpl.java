package com.juntai.tinder.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.juntai.soulboot.common.exception.SoulBootException;
import com.juntai.soulboot.data.ConditionParser;
import com.juntai.soulboot.util.JsonUtils;
import com.juntai.tinder.cache.EquipmentCache;
import com.juntai.tinder.condition.ForcesCondition;
import com.juntai.tinder.entity.*;
import com.juntai.tinder.exception.TinderErrorCode;
import com.juntai.tinder.facade.EquipmentTypeFacade;
import com.juntai.tinder.facade.ModelFacade;
import com.juntai.tinder.mapper.ForcesCarryMapper;
import com.juntai.tinder.mapper.ForcesMapper;
import com.juntai.tinder.mapper.ForcesPlanMapper;
import com.juntai.tinder.model.ForcesUpdateModel;
import com.juntai.tinder.model.ModelParameter;
import com.juntai.tinder.model.ModelParameterBase;
import com.juntai.tinder.model.Point;
import com.juntai.tinder.service.ForcesService;
import com.juntai.tinder.utils.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 搭载母体 服务实现类
 * </p>
 *
 * @author nemo
 * @since 2023-06-07
 */
@Service
public class ForcesServiceImpl implements ForcesService {

    @Autowired
    private ForcesMapper mapper;

    @Autowired
    private ModelFacade modelFacade;

    @Autowired
    private EquipmentTypeFacade equipmentTypeFacade;

    @Autowired
    private ForcesPlanMapper forcesPlanMapper;

    @Autowired
    private ForcesCarryMapper forcesCarryMapper;

    @Autowired
    private EquipmentCache equipmentCache;

    private Forces seek(Forces forces) {
        forces.setEquipmentTypeName(equipmentTypeFacade.getNameById(forces.getEquipmentType()));
        if (!StringUtils.isEmpty(forces.getInputInfo())) {
            forces.setInputParameter(JsonUtils.readList(forces.getInputInfo(), ModelParameterBase.class));
        }
        if (!StringUtils.isEmpty(forces.getOutputInfo())) {
            forces.setOutputParameter(JsonUtils.readList(forces.getOutputInfo(), ModelParameterBase.class));
        }
        Equipment cacheData = equipmentCache.getCacheData(forces.getEquipmentId());
        if (cacheData != null) {
            forces.setIconArmy(cacheData.getIconArmy());
            forces.setIcon3dUrl(cacheData.getIcon3dUrl());
        }
        List<ForcesCarry> carry = new LambdaQueryChainWrapper<>(forcesCarryMapper).eq(ForcesCarry::getBelongId, forces.getId())
                .eq(ForcesCarry::getExperimentId, forces.getExperimentId()).list();
        forces.setRelations(carry);
        return forces;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String copyForces(String id, String experimentId, Point point) {

        Forces byId = mapper.selectById(id);
        if (byId == null) {
            throw new SoulBootException(TinderErrorCode.TINDER_FORCES_ERROR, "没有该id的兵力");
        }
        byId = seek(byId);
        if (point != null) {
            byId.setLon(point.getLon() + 0.00001);
            byId.setLat(point.getLat() + 0.00001);
            byId.setAlt(point.getAlt());
        }
        if (StringUtils.isEmpty(experimentId)) {
            byId.setName(byId.getName() + "-copy");
            experimentId = byId.getExperimentId();
        }
        String experimentIdNew = experimentId;
        String forcesId = this.insert(byId);
        //处理兵力的搭载
        List<ForcesCarry> relations = byId.getRelations();
        if (!CollectionUtils.isEmpty(relations)) {
            relations.stream().forEach(q -> {
                q.setId(UUIDUtils.getHashUuid());
                q.setBelongId(forcesId);
                q.setExperimentId(experimentIdNew);
                forcesCarryMapper.insert(q);
            });
        }
        //处理兵力的任务
        List<ForcesPlan> plans = new LambdaQueryChainWrapper<>(forcesPlanMapper).eq(ForcesPlan::getForcesId, id).list();
        if (!CollectionUtils.isEmpty(plans)) {
            plans.stream().forEach(q -> {
                q.setId(UUIDUtils.getHashUuid());
                q.setForcesId(forcesId);
                q.setExperimentId(experimentIdNew);
                forcesPlanMapper.insert(q);
            });
        }

        return forcesId;
    }

    @Override
    public String copyById(String id, String experimentId) {
        return this.copyForces(id, experimentId, null);
    }

    @Override
    public Forces getById(String id) {
        return mapper.selectById(id);
    }

    @Override
    public String insert(Forces entity) {
        String id = UUIDUtils.getHashUuid();
        entity.setId(id);
        mapper.insert(entity);
        return id;
    }

    @Override
    public void insertList(List<Forces> entity) {
        entity.forEach(q -> {
            String id = UUIDUtils.getHashUuid();
            q.setId(id);
            mapper.insert(q);
        });
    }

    @Override
    public String addForces(String experimentId, String team, Equipment equipment, String modelId) {
        //判断是否有重名
        if (StringUtils.isBlank(equipment.getName())) {
            throw new SoulBootException(TinderErrorCode.TINDER_FORCES_ERROR, "名称不能为空");
        }
        Model model = modelFacade.seekById(modelId);
        if (model == null) {
            throw new SoulBootException(TinderErrorCode.TINDER_FORCES_ERROR, "模型数据为空");
        }
        List<Forces> query = new LambdaQueryChainWrapper<>(mapper).eq(Forces::getEquipmentId, experimentId).list();

        String name = equipment.getName();
        int suffixNum = 1;
        for (Forces forces : query) {
            String forceName = forces.getName();
            if (forces.getName().startsWith(name + "-")) {
                String suffix = forceName.replace(name + "-", "");
                try {
                    int num = Integer.parseInt(suffix);
                    if (num > suffixNum) {
                        suffixNum = num + 1;
                    }
                } catch (NumberFormatException e) {
                    continue;
                }
            }
        }
        equipment.setName(name + "-" + suffixNum);
        String id = UUIDUtils.getHashUuid();
        Forces forces = new Forces();
        forces.setTeam(team);
        forces.setExperimentId(experimentId);
        forces.setEquipmentType(equipment.getEquipmentType());
        forces.setName(equipment.getName());
        forces.setId(id);
        if (equipment.getDetail() != null) {
            forces.setAttributeInfo(equipment.getDetail().getAttributeInfo());
        }
        forces.setModelId(model.getId());
        forces.setInputInfo(model.getInputInfo());
        forces.setOutputInfo(model.getOutputInfo());
        mapper.insert(forces);
        return id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addForcesFromLibrary(String experimentId, String team, ForcesLibrary forcesLibrary) {
        Forces forces = new Forces();
        String forcesId = UUIDUtils.getHashUuid();
        forces.setTeam(team);
        forces.setExperimentId(experimentId);
        forces.setEquipmentType(forcesLibrary.getEquipmentType());
        forces.setEquipmentId(forcesLibrary.getEquipmentId());
        forces.setModelId(forcesLibrary.getModelId());
        forces.setName(forcesLibrary.getName());
        forces.setId(forcesId);
        forces.setAttributeInfo(forcesLibrary.getAttributeInfo());
        forces.setInputInfo(forcesLibrary.getInputInfo());
        forces.setOutputInfo(forcesLibrary.getOutputInfo());
        mapper.insert(forces);

        //关联新增搭载
        if (!CollectionUtils.isEmpty(forcesLibrary.getRelations())) {
            List<ForcesCarry> relations = toForcesLibrary(forcesId, forcesLibrary.getRelations());
            relations.forEach(q -> {
                forcesCarryMapper.insert(q);
            });
        }
        return forcesId;
    }

    @Override
    public void update(ForcesUpdateModel entity) {
        if (StringUtils.isBlank(entity.getId())) {
            throw new SoulBootException(TinderErrorCode.TINDER_FORCES_ERROR, "id 不能为空");
        }
        LambdaUpdateChainWrapper<Forces> wrapper = new LambdaUpdateChainWrapper<>(mapper);
        wrapper.eq(Forces::getId, entity.getId());

        if (entity.getAlt() != null) {
            wrapper.set(Forces::getAlt, entity.getAlt());
        }
        if (entity.getLon() != null) {
            wrapper.set(Forces::getLon, entity.getLon());
        }
        if (entity.getLat() != null) {
            wrapper.set(Forces::getLat, entity.getLat());
        }
        if (entity.getHeading() != null) {
            wrapper.set(Forces::getHeading, entity.getHeading());
        }
        if (entity.getSpeed() != null) {
            wrapper.set(Forces::getSpeed, entity.getSpeed());
        }
        if (entity.getLife() != null) {
            wrapper.set(Forces::getLife, entity.getLife());
        }
        if (!StringUtils.isBlank(entity.getName())) {
            wrapper.set(Forces::getName, entity.getName());
        }
        if (!StringUtils.isBlank(entity.getInputInfo())) {
            wrapper.set(Forces::getInputInfo, entity.getInputInfo());
        }
        wrapper.update();


    }

    @Override
    public void updateParentId(ForcesUpdateModel entity) {
        if (StringUtils.isBlank(entity.getId())) {
            throw new SoulBootException(TinderErrorCode.TINDER_FORCES_ERROR, "id 不能为空");
        }
        LambdaUpdateChainWrapper<Forces> wrapper = new LambdaUpdateChainWrapper<>(mapper);
        wrapper.eq(Forces::getId, entity.getId());
        wrapper.set(Forces::getParentId, entity.getParentId());
        wrapper.update();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(String id) {
        List<ForcesCarry> listForcesCarry = new LambdaQueryChainWrapper<>(forcesCarryMapper)
                .eq(ForcesCarry::getBelongId, id).list();
        forcesCarryMapper.deleteBatchIds(listForcesCarry.stream().map(ForcesCarry::getId).collect(Collectors.toList()));
        List<ForcesPlan> listForcesPlan = new LambdaQueryChainWrapper<>(forcesPlanMapper)
                .eq(ForcesPlan::getForcesId, id).list();
        forcesPlanMapper.deleteBatchIds(listForcesPlan.stream().map(ForcesPlan::getId).collect(Collectors.toList()));

        return mapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(List<String> ids) {
        List<ForcesCarry> listForcesCarry = new LambdaQueryChainWrapper<>(forcesCarryMapper)
                .in(ForcesCarry::getBelongId, ids).list();
        forcesCarryMapper.deleteBatchIds(listForcesCarry.stream().map(ForcesCarry::getId).collect(Collectors.toList()));
        List<ForcesPlan> listForcesPlan = new LambdaQueryChainWrapper<>(forcesPlanMapper)
                .in(ForcesPlan::getForcesId, ids).list();
        forcesPlanMapper.deleteBatchIds(listForcesPlan.stream().map(ForcesPlan::getId).collect(Collectors.toList()));
        return mapper.deleteBatchIds(ids);
    }

    @Override
    public List<Forces> list(ForcesCondition condition) {
        QueryChainWrapper<Forces> wrapper = ChainWrappers.queryChain(Forces.class);
        ;
        ConditionParser.parse(wrapper, condition);
        List<Forces> list = wrapper.orderByAsc("name").list();
        for (Forces force : list) {
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
        List<Forces> query = new LambdaQueryChainWrapper<>(mapper).eq(Forces::getEquipmentId, experimentId).list();
        query.forEach(q -> {
            q = seek(q);
        });
        return query;
    }

    @Override
    public List<String> queryByExperiment(String experimentId, String team) {
        LambdaQueryChainWrapper<Forces> wrapper = new LambdaQueryChainWrapper<>(mapper).eq(Forces::getEquipmentId, experimentId);
        if (!StringUtils.isBlank(team)) {
            wrapper.eq(Forces::getTeam, team);
        }
        List<Forces> query = wrapper.orderByAsc(Forces::getName).list();
        List<String> result = new ArrayList<>();
        for (Forces force : query) {
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
        if (byId == null) {
            return;
        }
        List<ModelParameter> oldParameters = JsonUtils.readList(byId.getInputInfo(), ModelParameter.class);

        Model model = modelFacade.getById(byId.getModelId());
        if (model == null || StringUtils.isEmpty(model.getInputInfo())) {
            return;
        }

        List<ModelParameter> modelParameters = JsonUtils.readList(model.getInputInfo(), ModelParameter.class);
        for (ModelParameter parameter : modelParameters) {
            ModelParameter temp = oldParameters.stream().filter(q -> StringUtils.equals(q.getName(), parameter.getName())).findFirst().orElse(null);
            if (temp == null) {
                oldParameters.add(parameter);
            }
        }
        String newInput = JsonUtils.write(oldParameters);
        new LambdaUpdateChainWrapper<>(mapper).eq(Forces::getId, forcesId)
                .set(Forces::getInputInfo, newInput).update();

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
