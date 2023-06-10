package com.juntai.tinder.controller;

import com.egova.web.annotation.Api;
import com.soul.tinder.entity.ForcesPlan;
import com.soul.tinder.service.ForcesPlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/3/22
 */
@Slf4j
@RestController
@RequestMapping("/unity/forces-plan")
@RequiredArgsConstructor
public class ForcePlanController {

    private final ForcesPlanService forcesPlanService;

    /**
     * 主键获取
     *
     * @param id 主键
     * @return MapSetting
     */
    @Api
    @GetMapping(value = "/{id}")
    public ForcesPlan getById(@PathVariable String id) {
        return forcesPlanService.getById(id);
    }

    @Api
    @DeleteMapping(value = "/{id}")
    public int deleteById(@PathVariable String id) {
        return forcesPlanService.deleteById(id);
    }

    @Api
    @GetMapping(value = "/forces")
    public List<ForcesPlan> queryByForcesId(@RequestParam(name = "experimentId") String experimentId,
                                            @RequestParam(name = "forcesId", required = false) String forcesId,
                                            @RequestParam(name = "team", required = false) String team
    ) {
        return forcesPlanService.queryByExperiment(experimentId, forcesId, team);
    }

    /**
     * 保存
     *
     * @param forcesPlan 试验计划
     * @return 主键
     */
    @Api
    @PostMapping
    public String insert(@RequestBody ForcesPlan forcesPlan) {
        return forcesPlanService.insert(forcesPlan);
    }

    /**
     * 更新
     *
     * @param forcesPlan 试验计划
     */
    @Api
    @PutMapping
    public void update(@RequestBody ForcesPlan forcesPlan) {
        forcesPlanService.update(forcesPlan);
    }


}
