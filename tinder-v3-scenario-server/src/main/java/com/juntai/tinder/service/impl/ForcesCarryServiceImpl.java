package com.juntai.tinder.service.impl;

import com.juntai.tinder.condition.ForcesCarryCondition;
import com.juntai.tinder.entity.ForcesCarry;
import com.juntai.tinder.mapper.ForcesCarryMapper;
import com.juntai.tinder.model.ForcesLibraryUpdateModel;
import com.juntai.tinder.service.ForcesCarryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

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
public class ForcesCarryServiceImpl  implements ForcesCarryService {

    @Override
    public ForcesCarry getById(String id) {
        return null;
    }

    @Override
    public String insert(ForcesCarry entity) {
        return null;
    }

    @Override
    public void insertList(List<ForcesCarry> entity) {

    }

    @Override
    public String addForcesCarry(String experimentId, String parentId, String equipmentId, String modelId, int num) {
        Equipment equipment = equipmentFacade.seekById(equipmentId);
        if(equipment == null){
            throw ExceptionUtils.api("没有相关的装备信息");
        }
        Model model = modelFacade.seekById(modelId);
        if (model == null) {
            throw ExceptionUtils.api("模型数据为空");
        }
        return addNewForcesCarry(experimentId,parentId,equipment,model,num);
    }
    public String addNewForcesCarry(String experimentId, String parentId, Equipment equipment, Model model, int num) {

        ForcesCarry forces = new ForcesCarry();
        forces.setExperimentId(experimentId);
        forces.setEquipmentType(equipment.getEquipmentType());
        forces.setEquipmentId(equipment.getId());
        forces.setBelongId(parentId);
        forces.setName(equipment.getName());
        forces.setNum(num);
        forces.setId(UUIDUtils.getHashUuid());
        forces.setModelId(model.getId());
        if (equipment.getDetail() != null) {
            forces.setAttributeInfo(equipment.getDetail().getAttributeInfo());
        }
        forces.setInputInfo(model.getInputInfo());
        forces.setOutputInfo(model.getOutputInfo());
        return super.insert(forces);
    }

    @Override
    public void update(String belongId, ForcesLibraryUpdateModel entity) {
        if (StringUtils.isBlank(entity.getId())) {
            throw ExceptionUtils.api("id 不能为空");
        }
        if (StringUtils.isBlank(belongId)){
            throw ExceptionUtils.api("belongId 不能为空");
        }
        ForcesLibraryCondition condition = new ForcesLibraryCondition();
        condition.setId(entity.getId());
        condition.setBelongId(belongId);
        Boolean exists = super.exists(condition);
        if(!exists){
            throw ExceptionUtils.api("不存在需要修改的数据");
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
    public void modifyList(String belongId, List<ForcesLibraryUpdateModel> list) {

        if (StringUtils.isBlank(belongId)){
            throw ExceptionUtils.api("belongId 不能为空");
        }
        for(ForcesLibraryUpdateModel model : list){
            if (StringUtils.isBlank(model.getId())) {
                continue;
            }
            ForcesLibraryCondition condition = new ForcesLibraryCondition();
            condition.setId(model.getId());
            condition.setBelongId(belongId);
            Boolean exists = super.exists(condition);
            if(!exists){
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
    public void save(List<ForcesCarry> list) {
        List<ForcesCarry> newCarry = new ArrayList<>();
        for (ForcesCarry carry : list) {
            if (StringUtils.isBlank(carry.getBelongId()) || StringUtils.isBlank(carry.getExperimentId())) {
                continue;
            }
            if (StringUtils.isBlank(carry.getId())) {
                carry.setId(UUIDUtils.getHashUuid());
                newCarry.add(carry);
                continue;
            }
            ForcesLibraryCondition condition = new ForcesLibraryCondition();
            condition.setId(carry.getId());
            condition.setBelongId(carry.getBelongId());
            Boolean exists = super.exists(condition);
            if (!exists) {
                continue;
            }
            HashMap<String, Object> map = new LinkedHashMap<>(16);
            if (!StringUtils.isBlank(carry.getName())) {
                map.put("name", carry.getName());
            }
            if (!StringUtils.isBlank(carry.getInputInfo())) {
                map.put("inputInfo", carry.getInputInfo());
            }
            if (carry.getNum() != null) {
                map.put("num", carry.getNum());
            }
            super.modify(map, condition);
        }
        super.insertList(newCarry);
    }

    @Override
    public int deleteById(String id) {
        return 0;
    }

    @Override
    public boolean exists(ForcesCarryCondition condition) {
        return super.exists(condition);
    }

    @Override
    public int deleteByIds(List<String> ids) {
        return 0;
    }

    @Override
    public List<ForcesCarry> list(ForcesCarryCondition condition) {
        return super.query(condition);
    }

    @Override
    public List<ForcesCarry> getByForcesId(String forcesId, String experimentId) {
        ForcesCarryCondition condition = new ForcesCarryCondition();
        condition.setBelongId(forcesId);
        condition.setExperimentId(experimentId);
        return super.query(condition);
    }
}
