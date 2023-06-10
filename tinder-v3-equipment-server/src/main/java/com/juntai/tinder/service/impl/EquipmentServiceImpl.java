package com.juntai.tinder.service.impl;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.juntai.soulboot.common.exception.SoulBootException;
import com.juntai.soulboot.data.ConditionParser;
import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.soulboot.security.UserContext;
import com.juntai.tinder.cache.EquipmentCache;
import com.juntai.tinder.condition.EquipmentCondition;
import com.juntai.tinder.entity.*;
import com.juntai.tinder.entity.enums.EquipmentKind;
import com.juntai.tinder.exception.TinderErrorCode;
import com.juntai.tinder.facade.EquipmentTypeFacade;
import com.juntai.tinder.mapper.*;
import com.juntai.tinder.service.EquipmentService;
import com.juntai.tinder.service.OrganizationService;
import com.juntai.tinder.utils.PinyingUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 分类id 服务实现类
 * </p>
 *
 * @author nemo
 * @since 2023-05-07
 */
@Service
public class EquipmentServiceImpl  implements EquipmentService {
    @Autowired
    private EquipmentMapper mapper;
    @Autowired
    private  EquipmentMapper equipmentMapper;
    @Autowired
    private EquipmentCache equipmentCache;
    @Autowired
    private EquipmentRelationMapper equipmentRelationMapper;
    @Autowired
    private EquipmentDetailMapper equipmentDetailMapper;
    @Autowired
    private EquipmentCarryMapper equipmentCarryMapper;
    @Autowired
    private RcsDataMapper rcsDataMapper;
    @Autowired
    private EquipmentTypeFacade equipmentTypeFacade;

    @Autowired
    private OrganizationService organizationService;
    @Override
    @Transactional(readOnly = true)
    public Equipment getById(String id) {
        Equipment equipment = mapper.selectById(id);

        if(equipment != null){
            Organization byId = organizationService.getById(equipment.getDepartmentId());
            equipment.setDepartmentName(byId==null ? "" : byId.getName());
            equipment.setEquipmentTypeName(equipmentTypeFacade.getNameById(equipment.getEquipmentType()));
            if(equipment.getCountry() != null){
                equipment.setCountryFlag(String.format("%s.jpg",equipment.getCountry().name()));
            }
        }

        return equipment;
    }

    @Override
    @Transactional(readOnly = true)
    public Equipment seekById(String id) {
        Equipment seek = seek(getById(id));
        if(seek != null){
            Organization byId = organizationService.getById(seek.getDepartmentId());
            seek.setDepartmentName(byId==null ? "" : byId.getName());
            seek.setEquipmentTypeName(equipmentTypeFacade.getNameById(seek.getEquipmentType()));
            if(seek.getCountry() != null){
                seek.setCountryFlag(String.format("%s.jpg",seek.getCountry().name()));
            }

        }
        return seek;
        
    }

    public Equipment seek(Equipment equipment){
        if (equipment == null) {
            return null;
        }
        List<EquipmentRelation> relations = new LambdaQueryChainWrapper<>(equipmentRelationMapper)
                .eq(EquipmentRelation::getBelongId,equipment.getId()).list();

        Map<EquipmentKind, List<EquipmentRelation>> relationsMap = relations.stream()
                .collect(Collectors.groupingBy(EquipmentRelation::getKind));

        EquipmentDetail equipmentDetail = new LambdaQueryChainWrapper<>(equipmentDetailMapper)
                .eq(EquipmentDetail::getEquipmentId, equipment.getId()).one();

        List<EquipmentCarry> carries = new LambdaQueryChainWrapper<>(equipmentCarryMapper)
                .eq(EquipmentCarry::getBelongId, equipment.getId())
                .eq(EquipmentCarry::getKind,"0").list();

        List<EquipmentCarry> weaponCarries = new LambdaQueryChainWrapper<>(equipmentCarryMapper)
                .eq(EquipmentCarry::getBelongId, equipment.getId())
                .eq(EquipmentCarry::getKind,"1").list();

        relations.forEach(equipmentRelation -> {
            if (equipmentRelation.getKind().equals(EquipmentKind.WEAPON)) {
                //属于武器，添加弹药搭载

                List<EquipmentCarry> equipmentCarries = new LambdaQueryChainWrapper<>(equipmentCarryMapper)
                        .eq(EquipmentCarry::getBelongId,equipmentRelation.getEquipmentId()).list();
                weaponCarries.addAll(equipmentCarries);
            }
        });

        List<RcsData> rcs = new LambdaQueryChainWrapper<>(rcsDataMapper).eq(RcsData::getGroupId,equipment.getId()).list();
        equipment.setRelations(relationsMap);
        equipment.setDetail(equipmentDetail);
        equipment.setCarries(carries);
        equipment.setWeaponCarries(weaponCarries);
        equipment.setRcs(rcs);
        return equipment;



    }

    @Override
    @Transactional(readOnly = true)
    public List<Equipment> list(EquipmentCondition condition) {
        QueryChainWrapper<Equipment> wrapper = ChainWrappers.queryChain(Equipment.class);;
        ConditionParser.parse(wrapper, condition);
        List<Equipment> list = wrapper.list();
        list.forEach(q->{
            Organization byId = organizationService.getById(q.getDepartmentId());
            q.setDepartmentName(byId==null ? "" : byId.getName());
            q.setEquipmentTypeName(equipmentTypeFacade.getNameById(q.getEquipmentType()));
            if(q.getCountry() != null){
                q.setCountryFlag(String.format("%s.jpg",q.getCountry().name()));
            }
        });
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public Pagination<Equipment> page(Query<EquipmentCondition, Equipment> query) {
        QueryChainWrapper<Equipment> wrapper = ChainWrappers.queryChain(Equipment.class);;
        ConditionParser.parse(wrapper, query.getCondition());
        Pagination<Equipment> page = wrapper.page(query.toPage());
        page.getList().forEach(q->{
            q.setDepartmentName("");
            q.setEquipmentTypeName("");
            q.setCountryFlag("");
        });
        return page;
    }

    @Override
    public int update(Equipment entity) {
        check(entity);
        entity.setModifier(UserContext.username());
        entity.setModifyTime(LocalDateTime.now());
        String pinyin = PinyingUtils.hanziToPinyin(entity.getName());
        entity.setPinyin(pinyin.substring(0, 1).toUpperCase());
        int i = mapper.updateById(entity);
        if (entity.getDetail() != null && !StringUtils.isBlank(entity.getDetail().getAttributeInfo())) {
            if (StringUtils.isBlank(entity.getDetail().getId())) {
                entity.getDetail().setId(UUID.randomUUID().toString());
                entity.getDetail().setEquipmentId(entity.getId());
                entity.getDetail().setDisplayUrl(getPicture(entity.getEquipmentType()));
                equipmentDetailMapper.insert(entity.getDetail());

            } else {
                LambdaUpdateChainWrapper<EquipmentDetail> lambdaUpdateChainWrapper = new LambdaUpdateChainWrapper<>(equipmentDetailMapper);
                lambdaUpdateChainWrapper.eq(EquipmentDetail::getId,entity.getDetail().getId())
                        .eq(EquipmentDetail::getEquipmentId,entity.getId())
                        .set(EquipmentDetail::getAttributeInfo,entity.getDetail().getAttributeInfo()).update();
            }

        }
        equipmentCache.setCacheData(entity.getId(), entity);
        return i;
    }

    @Override
    public String insert(Equipment entity) {


        check(entity);
        String id = getNewId(entity.getEquipmentType());
        entity.setCreator(UserContext.username());
        entity.setCreateTime(LocalDateTime.now());
        String pinyin = PinyingUtils.hanziToPinyin(entity.getName());
        entity.setPinyin(pinyin.substring(0, 1).toUpperCase());
        entity.setId(id);
        mapper.insert(entity);

        equipmentCache.setCacheData(id, entity);
        EquipmentDetail detail = new EquipmentDetail();
        detail.setId(UUID.randomUUID().toString());
        detail.setEquipmentId(entity.getId());
        detail.setDisplayUrl(getPicture(entity.getEquipmentType()));
        equipmentDetailMapper.insert(detail);
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
    public Map<String, String> getNameMapById() {
        return null;
    }

    @Override
    public Map<String, String> getDepartmentIdMapById() {
        return null;
    }

    @Override
    public List<Equipment> getAll() {
        return new LambdaQueryChainWrapper<>(equipmentMapper).list();
    }

    @Override
    public List<Equipment> getModelsByType(String equipmentType) {
        return new LambdaQueryChainWrapper<>(equipmentMapper)
                .likeRight(Equipment::getEquipmentType,equipmentType).list();
    }

    private String getPicture(String equipmentType) {
        if (equipmentType.startsWith("301")) {
            return "坦克.png";
        } else if (equipmentType.startsWith("30201")) {
            if (equipmentType.startsWith("3020101")) {
                return "航母.png";
            } else {
                return "舰船.png";
            }
        } else if (equipmentType.startsWith("30202")) {
            return "登陆舰.png";
        } else if (equipmentType.startsWith("30203")) {
            return "潜艇.png";
        } else if (equipmentType.startsWith("30204")) {
            return "无人艇.png";
        } else if (equipmentType.startsWith("30206")) {
            if (equipmentType.startsWith("3020602")) {
                return "甲藻受激光发光探测.png";
            }else if (equipmentType.startsWith("3020603")) {
                return "仿生附着网探测.png";
            } else if (equipmentType.startsWith("3020604")) {
                return "电测阵列探测.png";
            } else {
                return "default.png";
            }
        } else if (equipmentType.startsWith("30301")) {
            if (equipmentType.startsWith("3030101")) {
                return "预警机.png";
            } else {
                return "战斗机.png";
            }
        } else if (equipmentType.startsWith("30302")) {
            return "直升机.png";
        } else if (equipmentType.startsWith("30303")) {
            return "无人机.png";
        } else if (equipmentType.startsWith("304")) {
            return "卫星.png";
        } else if (equipmentType.startsWith("30501")) {
            return "导弹.png";
        } else if (equipmentType.startsWith("30502")) {
            return "鱼雷.png";
        } else if (equipmentType.startsWith("20101")) {
            return "雷达.png";
        } else if (equipmentType.startsWith("203")) {
            return "武器系统.png";
        } else {
            return "default.png";
        }
    }

    private String getNewId(String equipmentType) {
        List<Equipment> query = new LambdaQueryChainWrapper<>(equipmentMapper)
                .eq(Equipment::getEquipmentType,equipmentType).list();
        Long max = 0L;
        for (Equipment equipment : query) {
            String replace = equipment.getId().replaceFirst(equipmentType, "");
            Long aLong = Long.valueOf(replace);
            if (aLong > max) {
                max = aLong;
            }
        }
        return String.format("%s%s", equipmentType, StringUtils.leftPad(String.valueOf(max + 1), 2, "0"));


    }

    private void check(Equipment entity) {
        if (StringUtils.isAnyBlank(entity.getName(), entity.getDepartmentId())) {
            throw new SoulBootException(TinderErrorCode.TINDER_EQUIPMENT_ERROR,"名称或部门不能为空");
        }
        boolean exists = new LambdaQueryChainWrapper<>(equipmentMapper)
                .eq(Equipment::getName, entity.getName())
                .ne(Equipment::getId, entity.getId()).exists();

        if (exists) {
            throw new SoulBootException(TinderErrorCode.TINDER_EQUIPMENT_ERROR,"名称重复");
        }
    }

}
