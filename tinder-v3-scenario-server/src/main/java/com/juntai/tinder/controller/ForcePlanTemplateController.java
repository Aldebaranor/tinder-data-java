package com.juntai.tinder.controller;

import com.juntai.soulboot.web.api.ApiResultWrap;
import com.juntai.tinder.entity.ForcesPlanTemplate;
import com.juntai.tinder.service.ForcesPlanTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/3/22
 */
@ApiResultWrap
@RestController
@RequestMapping("/tinder/v3/forces-plan/template")
@RequiredArgsConstructor
public class ForcePlanTemplateController {

    private final ForcesPlanTemplateService forcesPlanService;

    /**
     * 主键获取
     *
     * @param id 主键
     * @return MapSetting
     */

    @GetMapping(value = "/{id}")
    public ForcesPlanTemplate getById(@PathVariable String id) {
        return forcesPlanService.getById(id);
    }


    @DeleteMapping(value = "/{id}")
    public int deleteById(@PathVariable String id) {
        return forcesPlanService.deleteById(id);
    }


    @GetMapping(value = "/forces")
    public List<ForcesPlanTemplate> queryByForcesId(@RequestParam(name = "experimentId") String experimentId,
                                                    @RequestParam(name = "team", required = false) String team
    ) {
        return forcesPlanService.queryByExperiment(experimentId, team);
    }

    /**
     * 保存
     *
     * @param forcesPlan 试验计划
     * @return 主键
     */

    @PostMapping
    public String insert(@RequestBody ForcesPlanTemplate forcesPlan) {
        return forcesPlanService.insert(forcesPlan);
    }

    /**
     * 更新
     *
     * @param forcesPlan 试验计划
     */

    @PutMapping
    public void update(@RequestBody ForcesPlanTemplate forcesPlan) {
        forcesPlanService.update(forcesPlan);
    }


}
