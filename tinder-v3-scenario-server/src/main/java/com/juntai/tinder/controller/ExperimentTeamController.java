package com.juntai.tinder.controller;

import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.soulboot.web.api.ApiResultWrap;
import com.juntai.tinder.condition.ExperimentTeamCondition;
import com.juntai.tinder.entity.ExperimentTeam;
import com.juntai.tinder.service.ExperimentTeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/5/17
 */
@ApiResultWrap
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

    @PostMapping
    public String insert(@RequestBody ExperimentTeam experimentTeam) {
        return experimentTeamService.insert(experimentTeam);
    }

    /**
     * 更新
     *
     * @param experimentTeam 试验分享
     */

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

    @PostMapping("/page")
    public Pagination<ExperimentTeam> page(@RequestBody Query<ExperimentTeamCondition, ExperimentTeam> model) {
        return experimentTeamService.page(model);
    }

    /**
     * 批量删除
     *
     * @param ids 主键列表
     * @return 影响记录行数
     */

    @PostMapping("/batch-delete")
    public int batchDelete(@RequestBody List<String> ids) {
        return experimentTeamService.deleteByIds(ids);
    }

    /**
     * 查询团队成员
     *
     * @param condition
     * @return List<ExperimentTeam>
     */

    @PostMapping("/list")
    public List<ExperimentTeam> list(@RequestBody ExperimentTeamCondition condition) {
        return experimentTeamService.list(condition);
    }


    @GetMapping("/team/{experimentId}")
    public String getTeam(@PathVariable String experimentId) {
        return experimentTeamService.getTeam(experimentId);
    }


}
