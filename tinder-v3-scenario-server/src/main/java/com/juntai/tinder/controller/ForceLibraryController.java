package com.juntai.tinder.controller;

import com.juntai.soulboot.web.api.ApiResultWrap;
import com.juntai.tinder.condition.ForcesLibraryCondition;
import com.juntai.tinder.entity.ForcesLibrary;
import com.juntai.tinder.model.ForcesEntity;
import com.juntai.tinder.model.ForcesLibraryUpdateModel;
import com.juntai.tinder.service.ForcesLibraryService;
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
@RequestMapping("/tinder/v3/forces-library")
@RequiredArgsConstructor
public class ForceLibraryController {

    private final ForcesLibraryService forcesLibraryService;

    /**
     * 主键获取
     *
     * @param id 主键
     * @return MapSetting
     */

    @GetMapping(value = "/{id}")
    public ForcesLibrary getById(@PathVariable String id) {
        return forcesLibraryService.getById(id);
    }

    /**
     * 主键获取，修改名称镜像
     *
     * @param id 主键
     * @return MapSetting
     */

    @GetMapping(value = "/new")
    public ForcesLibrary getByNewId(@RequestParam String id, @RequestParam String experimentId) {
        return forcesLibraryService.getNewId(id, experimentId);
    }

    /**
     * 根据试验id和阵营查询兵力以及兵力的搭载
     *
     * @param experimentId
     * @param team
     * @return
     */


    @GetMapping(value = "/list")
    public List<ForcesLibrary> list(@RequestParam(name = "experimentId") String experimentId,
                                    @RequestParam(name = "team", required = false) String team) {
        return forcesLibraryService.queryByExperiment(experimentId, team);
    }


    /**
     * 新增兵力
     *
     * @param entity
     * @return
     */

    @PostMapping("/add")
    public String insert(@RequestBody ForcesEntity entity) {

        return forcesLibraryService.addForces(entity.getExperimentId(), entity.getTeam(), entity.getEquipmentId(), entity.getModelId());
    }

    /**
     * 新增兵力搭载
     *
     * @param belongId
     * @param entity
     */

    @PostMapping("add/carry/{belongId}")
    public void insertRelation(@PathVariable String belongId, @RequestBody ForcesEntity entity) {
        forcesLibraryService.addRelation(entity.getExperimentId(), entity.getTeam(), entity.getEquipmentId(), entity.getModelId(), belongId, entity.getNum());
    }

    /**
     * 修改兵力基本信息
     *
     * @param model
     */

    @PostMapping(value = "/modify")
    public void update(@RequestBody ForcesLibraryUpdateModel model) {
        forcesLibraryService.update(model);
    }

    /**
     * 修改兵力名称
     *
     * @param id
     * @param name
     */

    @GetMapping(value = "/modify/name")
    public void updateName(@RequestParam String id, @RequestParam String name) {
        forcesLibraryService.updateName(id, name);
    }

    /**
     * 修改搭载
     *
     * @param belongId
     * @param entity
     */

    @PostMapping("modify/carry/{belongId}")
    public void updateRelation(@PathVariable String belongId, @RequestBody ForcesLibraryUpdateModel entity) {
        forcesLibraryService.updateCarry(belongId, entity);
    }


    /**
     * 修改搭载
     *
     * @param belongId
     * @param list
     */

    @PostMapping("modify/carry-list/{belongId}")
    public void updateRelationList(@PathVariable String belongId, @RequestBody List<ForcesLibraryUpdateModel> list) {
        forcesLibraryService.updateCarryList(belongId, list);
    }

    /**
     * 删除兵力
     *
     * @param id
     * @return
     */

    @DeleteMapping(value = "/{id}")
    public int deleteById(@PathVariable String id) {
        return forcesLibraryService.deleteById(id);
    }

    /**
     * 删除搭载
     *
     * @param id
     * @return
     */

    @DeleteMapping("{id}/carry/{belongId}")
    public int deleteRelation(@PathVariable String belongId, @PathVariable String id) {
        return forcesLibraryService.deleteCarryById(belongId, id);
    }

    /**
     * 根据兵力查询
     *
     * @param experimentId
     * @param forcesId
     * @return
     */

    @GetMapping(value = "/list/carry")
    public List<ForcesLibrary> getRelation(@RequestParam String experimentId, @RequestParam String forcesId) {
        ForcesLibraryCondition condition = new ForcesLibraryCondition();
        condition.setBelongId(forcesId);
        condition.setExperimentId(experimentId);
        return forcesLibraryService.list(condition);
    }

    /**
     * 刷新inputInfo
     *
     * @param forceId
     */

    @GetMapping(value = "/flash/inputInfo/{forceId}")
    public void flashInput(@PathVariable String forceId) {
        forcesLibraryService.flashInput(forceId);
    }


}
