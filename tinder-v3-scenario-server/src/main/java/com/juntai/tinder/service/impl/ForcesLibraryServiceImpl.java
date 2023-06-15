package com.juntai.tinder.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.juntai.soulboot.common.exception.SoulBootException;
import com.juntai.soulboot.data.ConditionParser;
import com.juntai.soulboot.util.JsonUtils;
import com.juntai.tinder.cache.EquipmentCache;
import com.juntai.tinder.condition.ForcesLibraryCondition;
import com.juntai.tinder.entity.Equipment;
import com.juntai.tinder.entity.Forces;
import com.juntai.tinder.entity.ForcesLibrary;
import com.juntai.tinder.entity.Model;
import com.juntai.tinder.exception.TinderErrorCode;
import com.juntai.tinder.facade.EquipmentFacade;
import com.juntai.tinder.facade.ModelFacade;
import com.juntai.tinder.mapper.ForcesLibraryMapper;
import com.juntai.tinder.mapper.ForcesMapper;
import com.juntai.tinder.model.ForcesLibraryUpdateModel;
import com.juntai.tinder.model.ModelParameter;
import com.juntai.tinder.service.ForcesLibraryService;
import com.juntai.tinder.utils.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author nemo
 * @since 2023-06-07
 */
@Service
public class ForcesLibraryServiceImpl implements ForcesLibraryService {

    @Autowired
    private ForcesLibraryMapper mapper;

    @Autowired
    private ForcesMapper forcesMapper;

    @Autowired
    private ModelFacade modelFacade;

    @Autowired
    private EquipmentFacade equipmentFacade;

    @Autowired
    private EquipmentCache equipmentCache;

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

    @Override
    public ForcesLibrary getById(String id) {
        return mapper.selectById(id);
    }

    @Override
    public ForcesLibrary getNewId(String id, String experimentId) {
        List<Forces> query = new LambdaQueryChainWrapper<>(forcesMapper).eq(Forces::getEquipmentId, experimentId).list();

        ForcesLibrary byId = this.getById(id);

        String name = byId.getName();
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
        byId.setName(name + "-" + suffixNum);
        return byId;
    }

    @Override
    public ForcesLibrary seekById(String id) {
        ForcesLibrary forcesLibrary = mapper.selectById(id);
        return seek(forcesLibrary);
    }

    private ForcesLibrary seek(ForcesLibrary forces) {

        Equipment cacheData = equipmentCache.getCacheData(forces.getEquipmentId());
        if (cacheData != null) {
            forces.setIconArmy(cacheData.getIconArmy());
            forces.setIcon3dUrl(cacheData.getIcon3dUrl());
        }
        List<ForcesLibrary> carry = new LambdaQueryChainWrapper<>(mapper).eq(ForcesLibrary::getBelongId, forces.getId())
                .list();
        forces.setRelations(carry);
        return forces;

    }

    @Override
    public String insert(ForcesLibrary entity) {
        String id = UUIDUtils.getHashUuid();
        entity.setId(UUIDUtils.getHashUuid());
        mapper.insert(entity);
        return id;
    }

    @Override
    public void insertList(List<ForcesLibrary> entity) {
        entity.forEach(q -> {
            q.setId(UUIDUtils.getHashUuid());
            mapper.insert(q);
        });
    }

    @Override
    public String copyForces(String id, String experimentId) {
        ForcesLibrary byId = mapper.selectById(id);
        if (byId == null) {
            throw new SoulBootException(TinderErrorCode.TINDER_FORCES_LIBRARY_ERROR, "没有该id的兵力");
        }
        if (StringUtils.isEmpty(experimentId)) {
            byId.setName(byId.getName() + "_copy");
            experimentId = byId.getExperimentId();
        }
        String experimentIdNew = experimentId;
        String forcesId = this.insert(byId);
        //处理兵力的搭载
        List<ForcesLibrary> relations = byId.getRelations();
        if (!CollectionUtils.isEmpty(relations)) {
            relations.forEach(q -> {
                q.setId(UUIDUtils.getHashUuid());
                q.setBelongId(forcesId);
                q.setExperimentId(experimentIdNew);
                mapper.insert(q);
            });
        }
        return forcesId;
    }

    @Override
    public String addForces(String experimentId, String team, String equipmentId, String modelId) {
        Equipment equipment = equipmentFacade.seekById(equipmentId);
        if (equipment == null) {
            throw new SoulBootException(TinderErrorCode.TINDER_FORCES_LIBRARY_ERROR, "没有相关的装备信息");
        }
        Model model = modelFacade.seekById(modelId);
        if (model == null) {
            throw new SoulBootException(TinderErrorCode.TINDER_FORCES_LIBRARY_ERROR, "模型数据为空");
        }
        return addNewForces(experimentId, team, equipment, model);
    }

    @Override
    public String addRelation(String experimentId, String team, String equipmentId, String modelId, String belongId, int num) {
        Equipment equipment = equipmentFacade.seekById(equipmentId);
        if (equipment == null) {
            throw new SoulBootException(TinderErrorCode.TINDER_FORCES_LIBRARY_ERROR, "没有相关的装备信息");
        }
        Model model = modelFacade.seekById(modelId);
        if (model == null) {
            throw new SoulBootException(TinderErrorCode.TINDER_FORCES_LIBRARY_ERROR, "模型数据为空");
        }
        return addNewRelation(experimentId, team, equipment, model, belongId, num);
    }

    @Override
    public int deleteById(String id) {
        LambdaQueryChainWrapper<ForcesLibrary> wrapper = new LambdaQueryChainWrapper<>(mapper);
        ;
        wrapper.eq(ForcesLibrary::getId, id).or().eq(ForcesLibrary::getBelongId, id);
        return mapper.delete(wrapper);

    }

    @Override
    public List<ForcesLibrary> list(ForcesLibraryCondition condition) {
        QueryChainWrapper<ForcesLibrary> wrapper = ChainWrappers.queryChain(ForcesLibrary.class);
        ;
        ConditionParser.parse(wrapper, condition);
        List<ForcesLibrary> list = wrapper.orderByAsc("name").list();
        for (ForcesLibrary library : list) {
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
            throw new SoulBootException(TinderErrorCode.TINDER_FORCES_LIBRARY_ERROR, "试验id不能为空");
        }
        LambdaQueryChainWrapper<ForcesLibrary> wrapper = new LambdaQueryChainWrapper<>(mapper).eq(ForcesLibrary::getExperimentId, experimentId);
        if (!StringUtils.isBlank(team)) {
            wrapper.eq(ForcesLibrary::getTeam, team);
        }
        List<ForcesLibrary> query = wrapper.orderByAsc(ForcesLibrary::getName).list();
        for (ForcesLibrary library : query) {
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
            throw new SoulBootException(TinderErrorCode.TINDER_FORCES_LIBRARY_ERROR, "id 不能为空");
        }
        LambdaUpdateChainWrapper<ForcesLibrary> wrapper = new LambdaUpdateChainWrapper<>(mapper)
                .eq(ForcesLibrary::getId, entity.getId());

        if (!StringUtils.isBlank(entity.getName())) {
            wrapper.set(ForcesLibrary::getName, entity.getName());
        }
        if (!StringUtils.isBlank(entity.getInputInfo())) {
            wrapper.set(ForcesLibrary::getInputInfo, entity.getInputInfo());
        }
        if (entity.getNum() != null) {
            wrapper.set(ForcesLibrary::getNum, entity.getNum());
        }
        wrapper.update();
    }

    @Override
    public void updateCarry(String belongId, ForcesLibraryUpdateModel entity) {
        if (StringUtils.isBlank(entity.getId())) {
            throw new SoulBootException(TinderErrorCode.TINDER_FORCES_LIBRARY_ERROR, "id 不能为空");
        }
        if (StringUtils.isBlank(belongId)) {
            throw new SoulBootException(TinderErrorCode.TINDER_FORCES_LIBRARY_ERROR, "belongId 不能为空");
        }
        LambdaUpdateChainWrapper<ForcesLibrary> wrapper = new LambdaUpdateChainWrapper<>(mapper).eq(ForcesLibrary::getId, entity.getId())
                .eq(ForcesLibrary::getBelongId, belongId);

        if (!StringUtils.isBlank(entity.getName())) {
            wrapper.set(ForcesLibrary::getName, entity.getName());
        }
        if (!StringUtils.isBlank(entity.getInputInfo())) {
            wrapper.set(ForcesLibrary::getInputInfo, entity.getInputInfo());
        }
        if (entity.getNum() != null) {
            wrapper.set(ForcesLibrary::getNum, entity.getNum());
        }
        wrapper.update();
    }

    @Override
    public void updateCarryList(String belongId, List<ForcesLibraryUpdateModel> list) {
        if (StringUtils.isBlank(belongId)) {
            throw new SoulBootException(TinderErrorCode.TINDER_FORCES_LIBRARY_ERROR, "belongId 不能为空");
        }
        for (ForcesLibraryUpdateModel model : list) {
            if (StringUtils.isBlank(model.getId())) {
                continue;
            }
            LambdaUpdateChainWrapper<ForcesLibrary> wrapper = new LambdaUpdateChainWrapper<>(mapper).eq(ForcesLibrary::getId, model.getId())
                    .eq(ForcesLibrary::getBelongId, belongId);

            if (!StringUtils.isBlank(model.getName())) {
                wrapper.set(ForcesLibrary::getName, model.getName());
            }
            if (!StringUtils.isBlank(model.getInputInfo())) {
                wrapper.set(ForcesLibrary::getInputInfo, model.getInputInfo());
            }
            if (model.getNum() != null) {
                wrapper.set(ForcesLibrary::getNum, model.getNum());
            }
            wrapper.update();
        }
    }

    @Override
    public int deleteCarryById(String belongId, String id) {
        if (StringUtils.isBlank(id)) {
            throw new SoulBootException(TinderErrorCode.TINDER_FORCES_LIBRARY_ERROR, "id 不能为空");
        }
        if (StringUtils.isBlank(belongId)) {
            throw new SoulBootException(TinderErrorCode.TINDER_FORCES_LIBRARY_ERROR, "belongId 不能为空");
        }
        List<ForcesLibrary> list = new LambdaQueryChainWrapper<>(mapper).eq(ForcesLibrary::getId, id)
                .eq(ForcesLibrary::getBelongId, belongId).list();
        if (CollectionUtils.isEmpty(list)) {
            throw new SoulBootException(TinderErrorCode.TINDER_FORCES_LIBRARY_ERROR, "没有该ID的兵力搭载");
        }
        return mapper.deleteBatchIds(list.stream().map(ForcesLibrary::getId).collect(Collectors.toList()));
    }

    @Override
    public void updateName(String id, String newName) {
        new LambdaUpdateChainWrapper<>(mapper).eq(ForcesLibrary::getId, id)
                .set(ForcesLibrary::getName, newName).update();
    }

    @Override
    public void flashInput(String forcesId) {

        ForcesLibrary byId = this.getById(forcesId);
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
        new LambdaUpdateChainWrapper<>(mapper).eq(ForcesLibrary::getId, forcesId)
                .set(ForcesLibrary::getInputInfo, newInput).update();
    }

    public String addNewForces(String experimentId, String team, Equipment equipment, Model model) {

        //判断是否有重名
        if (StringUtils.isBlank(equipment.getName())) {
            throw new SoulBootException(TinderErrorCode.TINDER_FORCES_LIBRARY_ERROR, "名称不能为空");
        }
        String id = UUIDUtils.getHashUuid();
        ForcesLibrary forces = new ForcesLibrary();
        forces.setTeam(team);
        forces.setExperimentId(experimentId);
        forces.setEquipmentType(equipment.getEquipmentType());
        forces.setEquipmentId(equipment.getId());

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

    public String addNewRelation(String experimentId, String team, Equipment equipment, Model model, String belongId, int num) {
        //判断是否有重名
        if (StringUtils.isBlank(equipment.getName())) {
            throw new SoulBootException(TinderErrorCode.TINDER_FORCES_LIBRARY_ERROR, "名称不能为空");
        }
        String id = UUIDUtils.getHashUuid();
        ForcesLibrary forces = new ForcesLibrary();
        forces.setTeam(team);
        forces.setExperimentId(experimentId);
        forces.setEquipmentType(equipment.getEquipmentType());
        forces.setEquipmentId(equipment.getId());
        forces.setName(equipment.getName());
        forces.setId(id);
        if (equipment.getDetail() != null) {
            forces.setAttributeInfo(equipment.getDetail().getAttributeInfo());
        }
        forces.setModelId(model.getId());
        forces.setInputInfo(model.getInputInfo());
        forces.setOutputInfo(model.getOutputInfo());
        forces.setBelongId(belongId);
        forces.setNum(num);
        mapper.insert(forces);
        return id;

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
