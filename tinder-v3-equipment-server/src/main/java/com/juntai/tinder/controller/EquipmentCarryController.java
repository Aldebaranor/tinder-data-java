package com.juntai.tinder.controller;

import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.tinder.cache.EquipmentCache;
import com.juntai.tinder.condition.EquipmentCarryCondition;
import com.juntai.tinder.condition.EquipmentRelationCondition;
import com.juntai.tinder.entity.Equipment;
import com.juntai.tinder.entity.EquipmentCarry;
import com.juntai.tinder.entity.EquipmentRelation;
import com.juntai.tinder.entity.enums.EquipmentKind;
import com.juntai.tinder.service.EquipmentCarryService;
import com.juntai.tinder.service.EquipmentRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author nemo
 * @since 2023-05-07
 */
@RestController
@RequestMapping("/tinder/v3/equipment-carry")
public class EquipmentCarryController {

    @Autowired
    private EquipmentCarryService equipmentCarryService;
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
    public EquipmentCarry getById(@PathVariable String id) {
        return equipmentCarryService.getById(id);
    }

    /**
     * 保存
     *
     * @param equipmentCarry 某个装备可搭载其他装备的关联关系表
     * @return 主键
     */

    @PostMapping
    public String insert(@RequestBody EquipmentCarry equipmentCarry) {
        return equipmentCarryService.insert(equipmentCarry);
    }


    @PostMapping(value = "/batch-insert")
    public void insertList(@RequestBody List<EquipmentCarry> list) {
        equipmentCarryService.insertList(list);
    }

    /**
     * 更新
     *
     * @param equipmentCarry 某个装备可搭载其他装备的关联关系表
     */
    @PutMapping
    public void update(@RequestBody EquipmentCarry equipmentCarry) {
        equipmentCarryService.update(equipmentCarry);
    }

    /**
     * 主键删除
     *
     * @param id 主键
     * @return 影响记录行数
     */

    @DeleteMapping(value = "/{id}")
    public int deleteById(@PathVariable String id) {
        return equipmentCarryService.deleteById(id);
    }

    /**
     * 分页查询
     *
     * @param model 模型
     * @return PageResult
     */

    @PostMapping("/page")
    public Pagination<EquipmentCarry> page(@RequestBody Query<EquipmentCarryCondition, EquipmentCarry> model) {
        return equipmentCarryService.page(model);
    }

    /**
     * 列表查询
     *
     * @param condition
     * @return
     */
    @PostMapping("/list")
    public List<EquipmentCarry> list(@RequestBody EquipmentCarryCondition condition) {
        return equipmentCarryService.list(condition);
    }

    /**
     * 批量删除
     *
     * @param ids 主键列表
     * @return 影响记录行数
     */

    @PostMapping("/batch-delete")
    public int batchDelete(@RequestBody List<String> ids) {
        return equipmentCarryService.deleteByIds(ids);
    }


    @GetMapping("/all-carry/{equipmentId}")
    public List<Equipment> list(@PathVariable String equipmentId) {
        EquipmentCarryCondition condition = new EquipmentCarryCondition();
        condition.setBelongId(equipmentId);
        List<EquipmentCarry> carryList = equipmentCarryService.list(condition);

        //添加该兵力武器系统的武器搭载
        EquipmentRelationCondition relationCondition = new EquipmentRelationCondition();
        relationCondition.setBelongId(equipmentId);
        List<EquipmentRelation> relations = equipmentRelationService.list(relationCondition);
        relations.forEach(r -> {
            if (r.getKind().equals(EquipmentKind.WEAPON)) {
                //属于武器，添加弹药搭载
                EquipmentCarryCondition weaponCondition = new EquipmentCarryCondition();
                weaponCondition.setBelongId(r.getEquipmentId());
                List<EquipmentCarry> equipmentCarries = equipmentCarryService.list(weaponCondition);
                carryList.addAll(equipmentCarries);
            }
        });
        List<Equipment> equipmentList = new ArrayList<>();
        //添加搭载
        for (EquipmentCarry carry : carryList) {
            Equipment cacheData = equipmentCache.getCacheData(carry.getEquipmentId());
            equipmentList.add(cacheData);
        }

        return equipmentList;
    }

}
