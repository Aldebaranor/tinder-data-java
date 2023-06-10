package com.juntai.tinder.controller;

import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.egova.web.annotation.Api;
import com.egova.web.annotation.RequestDecorating;
import com.juntai.tinder.service.ExperimentTeamService;
import com.soul.tinder.condition.ExperimentTeamCondition;
import com.soul.tinder.entity.ExperimentTeam;
import com.soul.tinder.service.ExperimentTeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/5/17
 */
@Slf4j
@RestController
@RequestMapping("/unity/experiment-team")
@RequiredArgsConstructor
public class ExperimentTeamController {

    private final ExperimentTeamService experimentTeamService;


    /**
     * 主键获取
     *
     * @param id 主键
     * @return ExperimentShare
     */
    @Api
    @GetMapping(value = "/{id}")
    public ExperimentTeam getById(@PathVariable String id) {
        return experimentTeamService.getById(id);
    }

    /**
     * 保存
     *
     * @param experimentTeam 试验分享
     * @return 主键
     */
    @Api
    @PostMapping
    public String insert(@RequestBody ExperimentTeam experimentTeam) {
        return experimentTeamService.insert(experimentTeam);
    }

    /**
     * 更新
     *
     * @param experimentTeam 试验分享
     */
    @Api
    @PutMapping
    public void update(@RequestBody ExperimentTeam experimentTeam) {
        experimentTeamService.update(experimentTeam);
    }

    /**
     * 主键删除
     *
     * @param id 主键
     * @return 影响记录行数
     */
    @Api
    @DeleteMapping(value = "/{id}")
    public int deleteById(@PathVariable String id) {
        return experimentTeamService.deleteById(id);
    }

    /**
     * 分页查询
     *
     * @param model 模型
     * @return PageResult
     */
    @Api
    @PostMapping("/page")
    public PageResult<ExperimentTeam> page(@RequestBody QueryModel<ExperimentTeamCondition> model) {
        return experimentTeamService.page(model);
    }

    /**
     * 批量删除
     *
     * @param ids 主键列表
     * @return 影响记录行数
     */
    @Api
    @PostMapping("/batch")
    @RequestDecorating(value = "delete")
    public int batchDelete(@RequestBody List<String> ids) {
        return experimentTeamService.deleteByIds(ids);
    }

    /**
     * 查询团队成员
     *
     * @param condition
     * @return List<ExperimentTeam>
     */
    @Api
    @PostMapping("/list")
    public List<ExperimentTeam> list(@RequestBody ExperimentTeamCondition condition) {
        return experimentTeamService.list(condition);
    }

    @Api
    @GetMapping("/team/{experimentId}")
    public String getTeam(@PathVariable String experimentId) {
        return experimentTeamService.getTeam(experimentId);
    }


}
