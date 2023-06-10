package com.juntai.tinder.controller;

import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.soulboot.web.api.ApiResultWrap;
import com.juntai.tinder.condition.EquipmentCondition;
import com.juntai.tinder.entity.Equipment;
import com.juntai.tinder.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 分类id 前端控制器
 * </p>
 *
 * @author nemo
 * @since 2023-05-07
 */
@ApiResultWrap
@RestController
@RequestMapping("/tinder/v3/equipment")
public class EquipmentController {

    @Autowired
    private  EquipmentService equipmentService;

    /**
     * 分页查询
     *
     * @return PageResult
     */
    @PostMapping("/page")
    public Pagination<Equipment> page(@RequestBody Query<EquipmentCondition, Equipment> query) {
        return equipmentService.page(query);
    }

    /**
     * 主键获取
     *
     * @param id 主键
     * @return OperateResult
     */
    @GetMapping("/{id}")
    public Equipment seekById(@PathVariable("id") String id) {
        return equipmentService.seekById(id);
    }


    /**
     * 保存
     *
     * @param entity
     * @return OperateResult
     */

    @PostMapping
    public String insert(@RequestBody Equipment entity) {
        return equipmentService.insert(entity);
    }

    /**
     * 更新
     *
     * @param entity
     * @return OperateResult
     */
    @PutMapping
    public void update(@RequestBody Equipment entity) {
        equipmentService.update(entity);
    }

    /**
     * 主键删除
     *
     * @param id 主键
     * @return OperateResult
     */

    @DeleteMapping("/{id}")
    public int deleteById(@PathVariable("id") String id) {
        return equipmentService.deleteById(id);
    }

    /**
     * 批量删除
     *
     * @param ids 主键列表
     * @return 影响记录行数
     */

    @DeleteMapping("/batch")
    public int deleteByIds(@RequestBody List<String> ids) {
        return equipmentService.deleteByIds(ids);
    }


    @GetMapping("/all")
    public List<Equipment> getAll() {
        return equipmentService.getAll();
    }

    @GetMapping("models")
    public List<Equipment> getModelsByType(@RequestParam("equipmentType") String equipmentType) {
        return equipmentService.getModelsByType(equipmentType);
    }

}
