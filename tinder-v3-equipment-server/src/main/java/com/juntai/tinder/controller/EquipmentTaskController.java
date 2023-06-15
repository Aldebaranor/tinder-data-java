package com.juntai.tinder.controller;

import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.soulboot.web.api.ApiResultWrap;
import com.juntai.tinder.condition.EquipmentTaskCondition;
import com.juntai.tinder.entity.EquipmentTask;
import com.juntai.tinder.service.EquipmentTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 装备名称 前端控制器
 * </p>
 *
 * @author nemo
 * @since 2023-05-07
 */
@ApiResultWrap
@RestController
@RequestMapping("/tinder/v3/equipment-task")
public class EquipmentTaskController {

    @Autowired
    private EquipmentTaskService equipmentTask;

    @GetMapping(value = "/{id}")
    public EquipmentTask getById(@PathVariable("id") String id) {
        return equipmentTask.getById(id);
    }

    /**
     * 保存
     *
     * @param entity
     * @return 主键
     */
    @PostMapping
    public String insert(@RequestBody EquipmentTask entity) {
        return equipmentTask.insert(entity);
    }

    /**
     * 更新
     *
     * @param entity
     */
    @PutMapping
    public void update(@RequestBody EquipmentTask entity) {
        equipmentTask.update(entity);
    }

    /**
     * 主键删除
     *
     * @param id 主键
     * @return 影响记录行数
     */
    @DeleteMapping(value = "/{id}")
    public int deleteById(@PathVariable String id) {
        return equipmentTask.deleteById(id);
    }

    /**
     * 分页查询
     *
     * @param task
     * @return
     */
    @PostMapping("/page")
    public Pagination<EquipmentTask> page(@RequestBody Query<EquipmentTaskCondition, EquipmentTask> task) {
        return equipmentTask.page(task);
    }
}
