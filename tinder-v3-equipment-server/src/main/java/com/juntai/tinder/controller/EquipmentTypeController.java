package com.juntai.tinder.controller;

import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.soulboot.web.api.ApiResultWrap;
import com.juntai.tinder.condition.EquipmentCondition;
import com.juntai.tinder.condition.EquipmentTypeCondition;
import com.juntai.tinder.entity.Equipment;
import com.juntai.tinder.entity.EquipmentType;
import com.juntai.tinder.service.EquipmentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author nemo
 * @since 2023-05-07
 */
@ApiResultWrap
@RestController
@RequestMapping("/tinder/v3/equipment-type")
public class EquipmentTypeController {


    @Autowired
    private EquipmentTypeService equipmentTypeService;

    /**
     * 主键获取
     *
     * @param id 主键
     * @return EquipmentType
     */
    @GetMapping(value = "/{id}")
    public EquipmentType getById(@PathVariable String id) {
        return equipmentTypeService.getById(id);
    }

    @GetMapping(value = "/{id}/name")
    public String getNameById(@PathVariable String id) {
        return equipmentTypeService.getNameById(id);
    }



    @GetMapping(value = "/{id}/children")
    public List<EquipmentType> children(@PathVariable String id) {
        return equipmentTypeService.children(id);
    }

    /**
     * 保存
     *
     * @param equipmentType 装备大小类表
     * @return 主键
     */
    @PostMapping
    public String insert(@RequestBody EquipmentType equipmentType) {
        return equipmentTypeService.insert(equipmentType);
    }

    /**
     * 更新
     *
     * @param equipmentType 装备大小类表
     */
    @PutMapping
    public void update(@RequestBody EquipmentType equipmentType) {
        equipmentTypeService.update(equipmentType);
    }

    /**
     * 主键删除
     *
     * @param id 主键
     * @return 影响记录行数
     */
    @DeleteMapping(value = "/{id}")
    public int deleteById(@PathVariable String id) {
        return equipmentTypeService.deleteById(id);
    }
//
//    @GetMapping({"/name-map/id"})
//    public Map<String, String> getNameMapById() {
//        return equipmentTypeService.getNameMapById();
//    }

    /**
     * 分页查询
     *
     * @param query 模型
     * @return PageResult
     */
    @PostMapping("/page")
    public Pagination<EquipmentType> page(@RequestBody Query<EquipmentTypeCondition, EquipmentType> query) {
        return equipmentTypeService.page(query);
    }

    /**
     * 批量删除
     *
     * @param ids 主键列表
     * @return 影响记录行数
     */
    @DeleteMapping("/batch")
    public int batchDelete(@RequestBody List<String> ids) {
        return equipmentTypeService.deleteByIds(ids);
    }

    /**
     * 树
     *
     * @return 树
     */

    @GetMapping("/tree")
    public List<EquipmentType> tree(@RequestParam(value = "hasEntity", required = false, defaultValue = "false") Boolean hasEntity
            , @RequestParam(value = "type", required = false) String type) {
        return equipmentTypeService.tree(hasEntity, type);
    }


    @GetMapping("/tree/{parentId}")
    public List<EquipmentType> tree(@PathVariable String parentId) {
        return equipmentTypeService.tree(parentId, false, null);
    }

}
