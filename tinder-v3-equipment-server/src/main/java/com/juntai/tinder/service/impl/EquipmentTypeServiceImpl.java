package com.juntai.tinder.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.juntai.soulboot.common.exception.SoulBootException;
import com.juntai.soulboot.data.ConditionParser;
import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.tinder.condition.EquipmentTypeCondition;
import com.juntai.tinder.entity.Equipment;
import com.juntai.tinder.entity.EquipmentType;
import com.juntai.tinder.entity.enums.CategoryType;
import com.juntai.tinder.exception.TinderErrorCode;
import com.juntai.tinder.mapper.EquipmentMapper;
import com.juntai.tinder.mapper.EquipmentTypeMapper;
import com.juntai.tinder.service.EquipmentTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author nemo
 * @since 2023-05-07
 */
@Service
public class EquipmentTypeServiceImpl implements EquipmentTypeService {
    @Autowired
    private EquipmentTypeMapper mapper;

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Override
    @Transactional(readOnly = true)
    public EquipmentType getById(String id) {
        return mapper.selectById(id);
    }

    @Override
    public List<EquipmentType> children(String id) {
        return  new LambdaQueryChainWrapper<>(mapper).eq(EquipmentType::getParentId,id).list();
    }

    @Override
    public List<EquipmentType> childrens(String id) {
        return new LambdaQueryChainWrapper<>(mapper).likeRight(EquipmentType::getParentId,id).list();
    }

//    @Override
//    public Map<String, String> getNameMapById() {
//        return null;
//    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipmentType> list(EquipmentTypeCondition condition) {
        QueryChainWrapper<EquipmentType> wrapper = ChainWrappers.queryChain(EquipmentType.class);;
        ConditionParser.parse(wrapper, condition);
        return wrapper.list();
    }

    @Override
    @Transactional(readOnly = true)
    public Pagination<EquipmentType> page(Query<EquipmentTypeCondition, EquipmentType> query) {
        QueryChainWrapper<EquipmentType> wrapper = ChainWrappers.queryChain(EquipmentType.class);;
        ConditionParser.parse(wrapper, query.getCondition());
        return wrapper.page(query.toPage());
    }



    @Override
    public int update(EquipmentType entity) {
        return mapper.updateById(entity);
    }

    @Override
    public String insert(EquipmentType entity) {
        String id = "";
        if (entity.getGrade() == null) {
            throw new SoulBootException(TinderErrorCode.TINDER_EQUIPMENT_TYPE_ERROR,"grade不能为空");
        }
        int order = 1;
        if (entity.getGrade() != 0) {
            String parentId = entity.getParentId();
            if (StringUtils.isBlank(parentId)) {
                throw new SoulBootException(TinderErrorCode.TINDER_EQUIPMENT_TYPE_ERROR,"非根节点的parentId不能为空");
            }
            EquipmentTypeCondition condition = new EquipmentTypeCondition();
            condition.setId(parentId);
            boolean exists = new LambdaQueryChainWrapper<>(mapper).eq(EquipmentType::getId, parentId).exists();
            if (!exists) {
                throw new SoulBootException(TinderErrorCode.TINDER_EQUIPMENT_TYPE_ERROR,"父节点parentId不存在");
            }
            List<EquipmentType> children = children(parentId);
            order = children.stream().mapToInt(EquipmentType::getSort).max().orElse(0) + 1;
            entity.setSort(order);
            id = String.format("%s%02d", parentId, order);
        } else {
            List<EquipmentType> children = children("");
            order = children.stream().mapToInt(EquipmentType::getSort).max().orElse(0) + 1;
            entity.setSort(order);
            id = String.format("%02d", order);
        }
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
    public List<EquipmentType> tree(String parentId, Boolean hasEntity, String type) {
        EquipmentType byId = getById(parentId);
        if (byId == null) {
            return null;
        }
        List<EquipmentType> list = new ArrayList<>();
        list.add(byId);
        List<EquipmentType> childrenList = childrens(parentId);
        if (CollectionUtils.isEmpty(childrenList)) {
            return list;
        }
        list.addAll(childrenList);
        return buildTree(
                list,
                hasEntity,
                type,
                (first, second) -> StringUtils.equals(first.getId(), second.getParentId()),
                item -> StringUtils.equals(item.getId(), parentId)
        );
    }

    @Override
    public List<EquipmentType> tree(Boolean hasEntity, String type) {
        return buildTree(
                new LambdaQueryChainWrapper<>(mapper).list(),
                hasEntity,
                type,
                (first, second) -> StringUtils.equals(first.getId(), second.getParentId()),
                item -> StringUtils.isBlank(item.getParentId())
        );

    }

    @Override
    public String getNameById(String id) {
        EquipmentType equipmentType = mapper.selectById(id);

        return equipmentType==null?null:equipmentType.getName();
    }

    /**
     * 构建树结构
     *
     * @param data              数据
     * @param hasEntity         是否需要查出叶子节点数据
     * @param type              类别
     * @param isParentPredicate 前者是否为后者的父级
     * @param isTopPredicate    元素是否为顶级
     * @return 树型结构
     */
    private List<EquipmentType> buildTree(
            List<EquipmentType> data,
            Boolean hasEntity,
            String type,
            BiPredicate<EquipmentType, EquipmentType> isParentPredicate,
            Predicate<EquipmentType> isTopPredicate
    ) {
        if (StringUtils.isNotBlank(type)) {
            String[] split = type.split(",");
            List<String> types = Arrays.stream(split).map(s -> CategoryType.findEnumByValueOrCode(s).getValue()).collect(Collectors.toList());
            data = data.stream().filter(equipmentType -> types.contains(equipmentType.getType().getValue())).collect(Collectors.toList());
        }
        List<Equipment> equipments = new ArrayList<>();
        if (hasEntity) {
            equipments = new LambdaQueryChainWrapper<>(equipmentMapper).list();
        }
        for (EquipmentType first : data) {
            if (first.getChildren() == null) {
                first.setChildren(new LinkedList<>());
            }
            if (hasEntity) {
                List<Equipment> es = equipments.stream().filter(equipment -> StringUtils.equals(equipment.getEquipmentType(), first.getId())).collect(Collectors.toList());
                first.setEquipments( es);
            }
            for (EquipmentType second : data) {
                // first 为 second 父级
                if (isParentPredicate.test(first, second)) {
                    first.getChildren().add(second);
                }
            }
        }
        // 返回顶级
        return data.stream().filter(isTopPredicate).collect(Collectors.toList());
    }
}
