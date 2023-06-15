package com.juntai.tinder.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.google.common.base.CaseFormat;
import com.juntai.soulboot.data.ConditionParser;
import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.tinder.cache.EquipmentCache;
import com.juntai.tinder.cache.ModelCache;
import com.juntai.tinder.condition.ModelCondition;
import com.juntai.tinder.entity.Equipment;
import com.juntai.tinder.entity.Model;
import com.juntai.tinder.entity.Organization;
import com.juntai.tinder.facade.ModelRelationFacade;
import com.juntai.tinder.mapper.ModelMapper;
import com.juntai.tinder.service.ModelService;
import com.juntai.tinder.service.OrganizationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 描述 服务实现类
 * </p>
 *
 * @author nemo
 * @since 2023-05-07
 */
@Service
public class ModelServiceImpl implements ModelService {
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ModelRelationFacade modelRelationFacade;
    @Autowired
    private EquipmentCache equipmentCache;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ModelCache modelCache;

    @Override
    @Transactional(readOnly = true)
    public Model getById(String id) {
        return mapper.selectById(id);
    }

    @Override
    public Model seek(Model model) {
        Equipment equipment = equipmentCache.getCacheData(model.getEquipmentId());
        if (equipment != null) {
            Organization organization = organizationService.getById(equipment.getDepartmentId());
            if (organization != null) {
                model.setDepartmentName(organization.getName());
            }
        }
        model.setRelations(modelRelationFacade.modelId(model.getId()));
        return model;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Model> list(ModelCondition condition) {
        QueryChainWrapper<Model> wrapper = ChainWrappers.queryChain(Model.class);
        ;
        ConditionParser.parse(wrapper, condition);
        return wrapper.list();
    }

    @Override
    @Transactional(readOnly = true)
    public Pagination<Model> page(Query<ModelCondition, Model> query) {
        QueryChainWrapper<Model> wrapper = ChainWrappers.queryChain(Model.class);
        ;
        ConditionParser.parse(wrapper, query.getCondition());
        return wrapper.page(query.toPage(Model.class));
    }


    @Override
    public int update(Model entity) {
        int i = mapper.updateById(entity);
        modelCache.clearCacheData(entity.getId());
        return i;
    }

    @Override
    public Model seekById(String id) {
        Model model = mapper.selectById(id);
        return seek(model);
    }

    @Override
    public List<Model> getAll() {
        List<Model> list = new LambdaQueryChainWrapper<>(mapper).list();
        list.forEach(q -> {
            q = seek(q);
        });
        return list;
    }

    @Override
    public Long count(ModelCondition condition) {
        QueryChainWrapper<Model> wrapper = ChainWrappers.queryChain(Model.class);
        ;
        ConditionParser.parse(wrapper, condition);
        return wrapper.count();
    }

    @Override
    public List<Model> getByEquipmentId(String equipmentId) {
        List<Model> list = new LambdaQueryChainWrapper<>(mapper).eq(Model::getEquipmentId, equipmentId).list();
        list.forEach(q -> {
            q = seek(q);
        });
        return list;
    }

    @Override
    public String insert(Model entity) {
        String id = UUID.randomUUID().toString();
        entity.setId(id);
        mapper.insert(entity);
        return id;
    }


    @Override
    public int deleteById(String id) {
        int i = mapper.deleteById(id);
        modelCache.clearCacheData(id);

        return i;
    }

    @Override
    public int deleteByIds(List<String> ids) {
        int i = mapper.deleteBatchIds(ids);
        modelCache.clearCacheDataList(ids);
        return i;
    }

    @Override
    public void modify(String id, HashMap<String, Object> map) {
        UpdateWrapper<Model> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", id);
        map.forEach((k, v) -> {
            k = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, k);
            wrapper.set(k, v);
        });
        mapper.update(null, wrapper);
    }
}
