package com.juntai.tinder.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.juntai.soulboot.common.exception.SoulBootException;
import com.juntai.soulboot.data.ConditionParser;
import com.juntai.tinder.condition.ForcesCarryCondition;
import com.juntai.tinder.entity.Equipment;
import com.juntai.tinder.entity.ForcesCarry;
import com.juntai.tinder.entity.Model;
import com.juntai.tinder.exception.TinderErrorCode;
import com.juntai.tinder.facade.EquipmentFacade;
import com.juntai.tinder.facade.ModelFacade;
import com.juntai.tinder.mapper.ForcesCarryMapper;
import com.juntai.tinder.model.ForcesLibraryUpdateModel;
import com.juntai.tinder.service.ForcesCarryService;
import com.juntai.tinder.utils.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class ForcesCarryServiceImpl implements ForcesCarryService {

    @Autowired
    private ForcesCarryMapper mapper;

    @Autowired
    private EquipmentFacade equipmentFacade;

    @Autowired
    private ModelFacade modelFacade;

    @Override
    public ForcesCarry getById(String id) {
        return null;
    }

    @Override
    public String insert(ForcesCarry entity) {
        String id = UUID.randomUUID().toString();
        entity.setId(id);
        mapper.insert(entity);
        return id;
    }

    @Override
    public void insertList(List<ForcesCarry> entity) {
        entity.forEach(q -> {
            String id = UUID.randomUUID().toString();
            q.setId(id);
            mapper.insert(q);
        });
    }

    @Override
    public String addForcesCarry(String experimentId, String parentId, String equipmentId, String modelId, int num) {
        Equipment equipment = equipmentFacade.seekById(equipmentId);
        if (equipment == null) {
            throw new SoulBootException(TinderErrorCode.TINDER_COMMON_ERROR, "没有相关的装备信息");
        }
        Model model = modelFacade.seekById(modelId);
        if (model == null) {
            throw new SoulBootException(TinderErrorCode.TINDER_COMMON_ERROR, "模型数据为空");
        }
        return addNewForcesCarry(experimentId, parentId, equipment, model, num);
    }

    public String addNewForcesCarry(String experimentId, String parentId, Equipment equipment, Model model, int num) {

        String id = UUIDUtils.getHashUuid();
        ForcesCarry forces = new ForcesCarry();
        forces.setExperimentId(experimentId);
        forces.setEquipmentType(equipment.getEquipmentType());
        forces.setEquipmentId(equipment.getId());
        forces.setBelongId(parentId);
        forces.setName(equipment.getName());
        forces.setNum(num);
        forces.setId(id);
        forces.setModelId(model.getId());
        if (equipment.getDetail() != null) {
            forces.setAttributeInfo(equipment.getDetail().getAttributeInfo());
        }
        forces.setInputInfo(model.getInputInfo());
        forces.setOutputInfo(model.getOutputInfo());
        mapper.insert(forces);
        return id;
    }

    @Override
    public void update(String belongId, ForcesLibraryUpdateModel entity) {
        if (StringUtils.isBlank(entity.getId())) {
            throw new SoulBootException(TinderErrorCode.TINDER_COMMON_ERROR, "id 不能为空");
        }
        if (StringUtils.isBlank(belongId)) {
            throw new SoulBootException(TinderErrorCode.TINDER_COMMON_ERROR, "belongId 不能为空");
        }

        boolean exists = new LambdaQueryChainWrapper<>(mapper).eq(ForcesCarry::getId, entity.getId())
                .eq(ForcesCarry::getBelongId, belongId).exists();
        if (!exists) {
            throw new SoulBootException(TinderErrorCode.TINDER_COMMON_ERROR, "不存在需要修改的数据");
        }
        LambdaUpdateChainWrapper<ForcesCarry> wrapper = new LambdaUpdateChainWrapper<>(mapper);
        wrapper.eq(ForcesCarry::getId, entity.getId());
        wrapper.eq(ForcesCarry::getBelongId, belongId);
        if (!StringUtils.isBlank(entity.getName())) {
            wrapper.set(ForcesCarry::getName, entity.getName());
        }
        if (!StringUtils.isBlank(entity.getInputInfo())) {
            wrapper.set(ForcesCarry::getInputInfo, entity.getInputInfo());
        }
        if (entity.getNum() != null) {
            wrapper.set(ForcesCarry::getNum, entity.getNum());
        }

        wrapper.update();

    }

    @Override
    public void modifyList(String belongId, List<ForcesLibraryUpdateModel> list) {

        if (StringUtils.isBlank(belongId)) {
            throw new SoulBootException(TinderErrorCode.TINDER_COMMON_ERROR, "belongId 不能为空");
        }
        for (ForcesLibraryUpdateModel model : list) {
            if (StringUtils.isBlank(model.getId())) {
                continue;
            }
            boolean exists = new LambdaQueryChainWrapper<>(mapper).eq(ForcesCarry::getId, model.getId())
                    .eq(ForcesCarry::getBelongId, belongId).exists();
            if (!exists) {
                continue;
            }
            LambdaUpdateChainWrapper<ForcesCarry> wrapper = new LambdaUpdateChainWrapper<>(mapper);
            wrapper.eq(ForcesCarry::getId, model.getId());
            wrapper.eq(ForcesCarry::getBelongId, belongId);
            if (!StringUtils.isBlank(model.getName())) {
                wrapper.set(ForcesCarry::getName, model.getName());
            }
            if (!StringUtils.isBlank(model.getInputInfo())) {
                wrapper.set(ForcesCarry::getInputInfo, model.getInputInfo());
            }
            if (model.getNum() != null) {
                wrapper.set(ForcesCarry::getNum, model.getNum());
            }
            wrapper.update();

        }
    }

    @Override
    public void save(List<ForcesCarry> list) {

        for (ForcesCarry carry : list) {
            if (StringUtils.isBlank(carry.getBelongId()) || StringUtils.isBlank(carry.getExperimentId())) {
                continue;
            }
            if (StringUtils.isBlank(carry.getId())) {
                carry.setId(UUIDUtils.getHashUuid());
                mapper.insert(carry);
                continue;
            }
            boolean exists = new LambdaQueryChainWrapper<>(mapper).eq(ForcesCarry::getId, carry.getId())
                    .eq(ForcesCarry::getBelongId, carry.getBelongId()).exists();
            if (!exists) {
                continue;
            }
            LambdaUpdateChainWrapper<ForcesCarry> wrapper = new LambdaUpdateChainWrapper<>(mapper);
            wrapper.eq(ForcesCarry::getId, carry.getId());
            wrapper.eq(ForcesCarry::getBelongId, carry.getBelongId());
            if (!StringUtils.isBlank(carry.getName())) {
                wrapper.set(ForcesCarry::getName, carry.getName());
            }
            if (!StringUtils.isBlank(carry.getInputInfo())) {
                wrapper.set(ForcesCarry::getInputInfo, carry.getInputInfo());
            }
            if (carry.getNum() != null) {
                wrapper.set(ForcesCarry::getNum, carry.getNum());
            }
            mapper.update(null, wrapper);

        }
    }

    @Override
    public int deleteById(String id) {
        return mapper.deleteById(id);
    }

    @Override
    public boolean exists(ForcesCarryCondition condition) {
        QueryChainWrapper<ForcesCarry> wrapper = ChainWrappers.queryChain(ForcesCarry.class);
        ;
        ConditionParser.parse(wrapper, condition);
        return wrapper.exists();
    }

    @Override
    public int deleteByIds(List<String> ids) {
        return mapper.deleteBatchIds(ids);
    }

    @Override
    public List<ForcesCarry> list(ForcesCarryCondition condition) {
        QueryChainWrapper<ForcesCarry> wrapper = ChainWrappers.queryChain(ForcesCarry.class);
        ;
        ConditionParser.parse(wrapper, condition);
        return wrapper.list();
    }

    @Override
    public List<ForcesCarry> getByForcesId(String forcesId, String experimentId) {
        return new LambdaQueryChainWrapper<>(mapper).eq(ForcesCarry::getBelongId, forcesId)
                .eq(ForcesCarry::getExperimentId, experimentId).list();
    }
}
