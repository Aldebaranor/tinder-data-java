package com.juntai.tinder.controller;

import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.soulboot.web.api.ApiResultWrap;
import com.juntai.tinder.condition.ArmyIconCondition;
import com.juntai.tinder.entity.ArmyIcon;
import com.juntai.tinder.service.ArmyIconService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author nemo
 * @since 2023-05-07
 */
@ApiResultWrap
@RestController
@RequestMapping("/tinder/v3/army-icon")
public class ArmyIconController {

    @Autowired
    private ArmyIconService armyIconService;


    @GetMapping(value = "/{id}")
    public ArmyIcon getById(@PathVariable("id") String id) {
        return armyIconService.getById(id);
    }

    /**
     * 保存
     *
     * @param entity
     * @return 主键
     */

    @PostMapping
    public String insert(@RequestBody ArmyIcon entity) {
        return armyIconService.insert(entity);
    }

    /**
     * 更新
     *
     * @param entity
     */

    @PutMapping
    public void update(@RequestBody ArmyIcon entity) {
        armyIconService.update(entity);
    }

    /**
     * 主键删除
     *
     * @param id 主键
     * @return 影响记录行数
     */

    @DeleteMapping(value = "/{id}")
    public int deleteById(@PathVariable String id) {
        return armyIconService.deleteById(id);
    }


    @PostMapping("/page")
    public Pagination<ArmyIcon> page(@RequestBody Query<ArmyIconCondition, ArmyIcon> model) {
        return armyIconService.page(model);
    }
}
