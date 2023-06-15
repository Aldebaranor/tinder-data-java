package com.juntai.tinder.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.juntai.soulboot.data.ConditionParser;
import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.tinder.cache.EquipmentCache;
import com.juntai.tinder.cache.ModelCache;
import com.juntai.tinder.condition.ModelRelationCondition;
import com.juntai.tinder.entity.Equipment;
import com.juntai.tinder.entity.Model;
import com.juntai.tinder.entity.ModelRelation;
import com.juntai.tinder.mapper.ModelRelationMapper;
import com.juntai.tinder.service.ModelRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author nemo
 * @since 2023-05-07
 */
@Service
public class ModelRelationServiceImpl implements ModelRelationService {
    @Autowired
    private ModelRelationMapper mapper;

    @Autowired
    private ModelCache modelCache;
    @Autowired
    private EquipmentCache equipmentCache;

    @Override
    @Transactional(readOnly = true)
    public ModelRelation getById(String id) {
        return mapper.selectById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ModelRelation> list(ModelRelationCondition condition) {
        QueryChainWrapper<ModelRelation> wrapper = ChainWrappers.queryChain(ModelRelation.class);
        ;
        ConditionParser.parse(wrapper, condition);
        return wrapper.list();
    }

    @Override
    @Transactional(readOnly = true)
    public Pagination<ModelRelation> page(Query<ModelRelationCondition, ModelRelation> query) {
        QueryChainWrapper<ModelRelation> wrapper = ChainWrappers.queryChain(ModelRelation.class);
        ;
        ConditionParser.parse(wrapper, query.getCondition());
        return wrapper.page(query.toPage(ModelRelation.class));
    }


    @Override
    public int update(ModelRelation entity) {
        return mapper.updateById(entity);
    }

    @Override
    public String insert(ModelRelation entity) {
        String id = UUID.randomUUID().toString();
        entity.setId(id);
        mapper.insert(entity);
        return id;
    }


    @Override
    public int deleteById(String id) {
        return mapper.deleteById(id);

    }

    @Override
    public int deleteByIds(List<String> ids) {
        return mapper.deleteBatchIds(ids);
    }

    @Override
    public ModelRelation seekById(String id) {
        ModelRelation modelRelation = mapper.selectById(id);
        return seek(modelRelation);
    }

    public ModelRelation seek(ModelRelation modelRelation) {
        if (modelRelation != null) {
            Model cacheData = modelCache.getCacheData(modelRelation.getMemberId());
            if (cacheData != null) {
                modelRelation.setMemberKind(getKindByEquipmentId(cacheData.getEquipmentId()));
                Equipment equipment = equipmentCache.getCacheData(cacheData.getEquipmentId());
                if (equipment != null) {
                    modelRelation.setMemberName(equipment.getName());
                }
                modelRelation.setMemberVersion(cacheData.getVersion());
            }
        }
        return modelRelation;
    }

    @Override
    public List<ModelRelation> modelId(String modelId) {
        List<ModelRelation> list = new LambdaQueryChainWrapper<>(mapper).eq(ModelRelation::getBelongId, modelId).list();
        list.forEach(q -> {
            q = seek(q);
        });
        return list;
    }

    public String getKindByEquipmentId(String equipmentId) {
        if (equipmentId.startsWith("2")) {
            if (equipmentId.startsWith("201")) {
                if (equipmentId.startsWith("20101")) {
                    return "SENSOR1";
                } else if (equipmentId.startsWith("20102")) {
                    return "SENSOR2";
                } else if (equipmentId.startsWith("20104")) {
                    return "SENSOR3";
                } else if (equipmentId.startsWith("20105")) {
                    return "SENSOR4";
                } else if (equipmentId.startsWith("20103")) {
                    return "SENSOR5";
                } else {
                    return "SENSOR6";
                }

            }
            if (equipmentId.startsWith("203")) {
                if (equipmentId.startsWith("20302")) {
                    return "WEAPON1";
                } else if (equipmentId.startsWith("20301")) {
                    return "WEAPON2";
                } else if (equipmentId.startsWith("20305")) {
                    return "WEAPON3";
                } else if (equipmentId.startsWith("20303")) {
                    return "WEAPON4";
                } else {
                    return "WEAPON6";
                }
            }
            if (equipmentId.startsWith("202")) {
                return "WEAPON5";
            }
            if (equipmentId.startsWith("204")) {
                if (equipmentId.startsWith("20401")) {
                    return "COMM1";
                } else if (equipmentId.startsWith("20402")) {
                    return "COMM2";
                } else if (equipmentId.startsWith("20403")) {
                    return "COMM3";
                } else if (equipmentId.startsWith("20404")) {
                    return "COMM4";
                } else {
                    return "COMM5";
                }

            }
            if (equipmentId.startsWith("205")) {
                return "OTHER1";
            }
            if (equipmentId.startsWith("206")) {
                return "OTHER2";
            }
            if (equipmentId.startsWith("208")) {
                return "OTHER3";
            }
            if (equipmentId.startsWith("207")) {
                return "COMMAND";
            } else {
                return "OTHER4";
            }
        } else {
            return "OTHER4";
        }
    }
}
