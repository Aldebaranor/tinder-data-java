package com.juntai.tinder.controller;

import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.tinder.cache.EquipmentCache;
import com.juntai.tinder.condition.EquipmentRelationCondition;
import com.juntai.tinder.entity.Equipment;
import com.juntai.tinder.entity.EquipmentRelation;
import com.juntai.tinder.service.EquipmentRelationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author nemo
 * @since 2023-05-07
 */
@RestController
@RequestMapping("/tinder/v3/equipment-relation")
public class EquipmentRelationController {

    @Autowired
    private EquipmentRelationService equipmentRelationService;
    @Autowired
    private EquipmentCache equipmentCache;


    /**
     * 主键获取
     *
     * @param id 主键
     * @return EquipmentRelation
     */

    @GetMapping(value = "/{id}")
    public EquipmentRelation getById(@PathVariable String id) {
        return equipmentRelationService.getById(id);
    }

    /**
     * 保存
     *
     * @param equipmentRelation 某个装备可搭载其他装备的关联关系表
     * @return 主键
     */

    @PostMapping
    public String insert(@RequestBody EquipmentRelation equipmentRelation) {
        return equipmentRelationService.insert(equipmentRelation);
    }


    @PostMapping(value = "/batch-insert")
    public void insertList(@RequestBody List<EquipmentRelation> list) {
        equipmentRelationService.insertList(list);
    }

    /**
     * 更新
     *
     * @param equipmentRelation 某个装备可搭载其他装备的关联关系表
     */

    @PutMapping
    public void update(@RequestBody EquipmentRelation equipmentRelation) {
        equipmentRelationService.update(equipmentRelation);
    }

    /**
     * 主键删除
     *
     * @param id 主键
     * @return 影响记录行数
     */

    @DeleteMapping(value = "/{id}")
    public int deleteById(@PathVariable String id) {
        return equipmentRelationService.deleteById(id);
    }

    /**
     * 分页查询
     *
     * @param model 模型
     * @return PageResult
     */

    @PostMapping("/page")
    public Pagination<EquipmentRelation> page(@RequestBody Query<EquipmentRelationCondition, EquipmentRelation> model) {
        return equipmentRelationService.page(model);
    }

    /**
     * 列表查询
     *
     * @param condition
     * @return
     */

    @PostMapping("/list")
    public List<EquipmentRelation> list(@RequestBody EquipmentRelationCondition condition) {
        return equipmentRelationService.list(condition);
    }

    /**
     * 批量删除
     *
     * @param ids 主键列表
     * @return 影响记录行数
     */

    @PostMapping("/batch-delete")
    public int batchDelete(@RequestBody List<String> ids) {
        return equipmentRelationService.deleteByIds(ids);
    }


    @GetMapping("/all-relation/{equipmentId}")
    public List<Equipment> list(@PathVariable String equipmentId) {
        EquipmentRelationCondition condition = new EquipmentRelationCondition();
        condition.setBelongId(equipmentId);
        List<EquipmentRelation> relations = equipmentRelationService.list(condition);
        List<Equipment> equipmentList = new ArrayList<>();
        for (EquipmentRelation relation : relations) {
            Equipment cacheData = equipmentCache.getCacheData(relation.getEquipmentId());
            equipmentList.add(cacheData);
        }
        return equipmentList;
    }

    @GetMapping("/key-relation/{equipmentId}")
    public List<Equipment> list(@PathVariable String equipmentId, @RequestParam String keyword) {
        EquipmentRelationCondition condition = new EquipmentRelationCondition();
        condition.setBelongId(equipmentId);
        List<EquipmentRelation> relations = equipmentRelationService.list(condition);
        List<Equipment> equipmentList = new ArrayList<>();
        for (EquipmentRelation relation : relations) {
            Equipment cacheData = equipmentCache.getCacheData(relation.getEquipmentId());
            equipmentList.add(cacheData);
        }
        if (StringUtils.equals(keyword, "SENSOR1")) {
            return equipmentList.stream().filter(q -> q.getEquipmentType().startsWith("20101")).collect(Collectors.toList());
        } else if (StringUtils.equals(keyword, "SENSOR2")) {
            return equipmentList.stream().filter(q -> q.getEquipmentType().startsWith("20102")).collect(Collectors.toList());
        } else if (StringUtils.equals(keyword, "SENSOR3")) {
            return equipmentList.stream().filter(q -> q.getEquipmentType().startsWith("20104")).collect(Collectors.toList());
        } else if (StringUtils.equals(keyword, "SENSOR4")) {
            return equipmentList.stream().filter(q -> q.getEquipmentType().startsWith("20105")).collect(Collectors.toList());
        } else if (StringUtils.equals(keyword, "SENSOR5")) {
            return equipmentList.stream().filter(q -> q.getEquipmentType().startsWith("20103")).collect(Collectors.toList());
        } else if (StringUtils.equals(keyword, "SENSOR6")) {
            return equipmentList.stream().filter(q -> q.getEquipmentType().startsWith("201")
                    && !q.getEquipmentType().startsWith("20101")
                    && !q.getEquipmentType().startsWith("20102")
                    && !q.getEquipmentType().startsWith("20103")
                    && !q.getEquipmentType().startsWith("20104")
                    && !q.getEquipmentType().startsWith("20105")
            ).collect(Collectors.toList());
        } else if (StringUtils.equals(keyword, "WEAPON1")) {
            return equipmentList.stream().filter(q -> q.getEquipmentType().startsWith("20302")).collect(Collectors.toList());
        } else if (StringUtils.equals(keyword, "WEAPON2")) {
            return equipmentList.stream().filter(q -> q.getEquipmentType().startsWith("20301")).collect(Collectors.toList());
        } else if (StringUtils.equals(keyword, "WEAPON3")) {
            return equipmentList.stream().filter(q -> q.getEquipmentType().startsWith("20305")).collect(Collectors.toList());
        } else if (StringUtils.equals(keyword, "WEAPON4")) {
            return equipmentList.stream().filter(q -> q.getEquipmentType().startsWith("20303")).collect(Collectors.toList());
        } else if (StringUtils.equals(keyword, "WEAPON5")) {
            return equipmentList.stream().filter(q -> q.getEquipmentType().startsWith("202")).collect(Collectors.toList());
        } else if (StringUtils.equals(keyword, "WEAPON6")) {
            return equipmentList.stream().filter(q -> q.getEquipmentType().startsWith("203")
                    && !q.getEquipmentType().startsWith("20301")
                    && !q.getEquipmentType().startsWith("20302")
                    && !q.getEquipmentType().startsWith("20303")
                    && !q.getEquipmentType().startsWith("20304")
            ).collect(Collectors.toList());
        } else if (StringUtils.equals(keyword, "COMM1")) {
            return equipmentList.stream().filter(q -> q.getEquipmentType().startsWith("20401")).collect(Collectors.toList());
        } else if (StringUtils.equals(keyword, "COMM2")) {
            return equipmentList.stream().filter(q -> q.getEquipmentType().startsWith("20402")).collect(Collectors.toList());
        } else if (StringUtils.equals(keyword, "COMM3")) {
            return equipmentList.stream().filter(q -> q.getEquipmentType().startsWith("20403")).collect(Collectors.toList());
        } else if (StringUtils.equals(keyword, "COMM4")) {
            return equipmentList.stream().filter(q -> q.getEquipmentType().startsWith("20404")).collect(Collectors.toList());
        } else if (StringUtils.equals(keyword, "COMM5")) {
            return equipmentList.stream().filter(q -> q.getEquipmentType().startsWith("204")
                    && !q.getEquipmentType().startsWith("20401")
                    && !q.getEquipmentType().startsWith("20402")
                    && !q.getEquipmentType().startsWith("20403")
                    && !q.getEquipmentType().startsWith("20404")
            ).collect(Collectors.toList());
        } else if (StringUtils.equals(keyword, "OTHER1")) {
            return equipmentList.stream().filter(q -> q.getEquipmentType().startsWith("205")).collect(Collectors.toList());
        } else if (StringUtils.equals(keyword, "OTHER2")) {
            return equipmentList.stream().filter(q -> q.getEquipmentType().startsWith("206")).collect(Collectors.toList());
        } else if (StringUtils.equals(keyword, "OTHER3")) {
            return equipmentList.stream().filter(q -> q.getEquipmentType().startsWith("208")).collect(Collectors.toList());
        } else if (StringUtils.equals(keyword, "COMMAND")) {
            return equipmentList.stream().filter(q -> q.getEquipmentType().startsWith("207")).collect(Collectors.toList());
        } else if (StringUtils.equals(keyword, "OTHER4")) {
            return equipmentList.stream().filter(q -> q.getEquipmentType().startsWith("2")
                    && !q.getEquipmentType().startsWith("201")
                    && !q.getEquipmentType().startsWith("202")
                    && !q.getEquipmentType().startsWith("203")
                    && !q.getEquipmentType().startsWith("204")
                    && !q.getEquipmentType().startsWith("205")
                    && !q.getEquipmentType().startsWith("206")
                    && !q.getEquipmentType().startsWith("207")
                    && !q.getEquipmentType().startsWith("208")
            ).collect(Collectors.toList());
        } else {
            return null;
        }
    }

}
