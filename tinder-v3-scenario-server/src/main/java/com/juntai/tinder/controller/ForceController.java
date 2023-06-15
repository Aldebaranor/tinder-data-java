package com.juntai.tinder.controller;


import com.juntai.soulboot.common.exception.SoulBootException;
import com.juntai.soulboot.web.api.ApiResultWrap;
import com.juntai.tinder.condition.ForcesCarryCondition;
import com.juntai.tinder.condition.ForcesCondition;
import com.juntai.tinder.entity.Forces;
import com.juntai.tinder.entity.ForcesCarry;
import com.juntai.tinder.entity.ForcesLibrary;
import com.juntai.tinder.exception.TinderErrorCode;
import com.juntai.tinder.facade.EquipmentTaskFacade;
import com.juntai.tinder.model.ForcesEntity;
import com.juntai.tinder.model.ForcesLibraryUpdateModel;
import com.juntai.tinder.model.ForcesUpdateModel;
import com.juntai.tinder.model.Point;
import com.juntai.tinder.service.ForcesCarryService;
import com.juntai.tinder.service.ForcesService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/3/22
 */
@ApiResultWrap
@RestController
@RequestMapping("/tinder/v3/forces")
@RequiredArgsConstructor
public class ForceController {

    private final ForcesService forcesService;

    private final ForcesCarryService forcesCarryService;

    private final EquipmentTaskFacade equipmentTaskService;


    /**
     * 主键获取
     *
     * @param id 主键
     * @return MapSetting
     */

    @GetMapping(value = "/{id}")
    public Forces getById(@PathVariable String id) {
        return forcesService.getById(id);
    }

    /**
     * 根据Id查询搭载详细信息
     *
     * @param id
     * @return
     */

    @GetMapping(value = "/{id}/carry")
    public ForcesCarry getCarryById(@PathVariable String id) {
        return forcesCarryService.getById(id);
    }


    @PostMapping(value = "/{id}/copy")
    public String copyById(@PathVariable String id, @RequestBody Point point) {
        return forcesService.copyForces(id, null, point);
    }


    @GetMapping(value = "/{id}/copy")
    public String copyById(@PathVariable String id) {
        return forcesService.copyForces(id, null, null);
    }


    /**
     * 获取兵力
     *
     * @param experimentId 试验ID
     * @param team         阵营
     * @return 格式与仿真输出的一致
     */

    @GetMapping(value = "/list")
    public List<String> list(@RequestParam(name = "experimentId") String experimentId,
                             @RequestParam(name = "team", required = false) String team) {
        return forcesService.queryByExperiment(experimentId, team);
    }

    /**
     * 获取兵力的详细信息
     *
     * @param experimentId
     * @param team
     * @return
     */

    @GetMapping(value = "/all")
    public List<Forces> getAll(@RequestParam(name = "experimentId") String experimentId,
                               @RequestParam(name = "team", required = false) String team) {

        ForcesCondition condition = new ForcesCondition();
        condition.setExperimentId(experimentId);
        if (!StringUtils.isBlank(team)) {
            condition.setTeam(team);
        }

        return forcesService.list(condition);
    }


    @GetMapping(value = "/incapacity/list")
    public List<Forces> getIncapacity(@RequestParam(name = "experimentId") String experimentId) {
        ForcesCondition condition = new ForcesCondition();
        condition.setExperimentId(experimentId);

        List<Forces> list = forcesService.list(condition);
        List<String> equipments = equipmentTaskService.containTask("0100");
        if (CollectionUtils.isEmpty(equipments)) {
            return null;
        }
        return list.stream().filter(q -> equipments.contains(q.getEquipmentId())).collect(Collectors.toList());
    }

    @GetMapping(value = "/reconnection/list")
    public List<Forces> getReconnection(@RequestParam(name = "experimentId") String experimentId) {
        ForcesCondition condition = new ForcesCondition();
        condition.setExperimentId(experimentId);

        List<Forces> list = forcesService.list(condition);
        List<String> equipments = equipmentTaskService.containTask("0101");
        if (CollectionUtils.isEmpty(equipments)) {
            return null;
        }
        return list.stream().filter(q -> equipments.contains(q.getEquipmentId())).collect(Collectors.toList());
    }

    /**
     * 从兵力资源库添加兵力到试验
     *
     * @param entity
     * @return
     */

    @PostMapping("/add")
    public String insertFromLibrary(@RequestBody ForcesLibrary entity) {
        return forcesService.addForcesFromLibrary(entity.getExperimentId(), entity.getTeam(), entity);
    }


    /**
     * 新增搭载
     *
     * @param entity
     */

    @PostMapping("add/carry/{belongId}")
    public void insertRelation(@PathVariable String belongId, @RequestBody ForcesEntity entity) {
        forcesCarryService.addForcesCarry(entity.getExperimentId(), belongId, entity.getEquipmentId(), entity.getModelId(), entity.getNum());
    }

    /**
     * 修改兵力
     *
     * @param model
     */

    @PostMapping(value = "/modify")
    public void update(@RequestBody ForcesUpdateModel model) {
        forcesService.update(model);
    }

    /**
     * 刷新inputInfo
     *
     * @param forceId
     */

    @GetMapping(value = "/flash/inputInfo/{forceId}")
    public void flashInput(@PathVariable String forceId) {
        forcesService.flashInput(forceId);
    }


    /**
     * 修改兵力 只更新经纬度高度航向
     *
     * @param model
     */

    @PostMapping(value = "/modify/parentId")
    public void updateIgnore(@RequestBody ForcesUpdateModel model) {
        forcesService.updateParentId(model);
    }

    /**
     * 修改搭载
     *
     * @param entity
     */

    @PostMapping("/modify/carry/{belongId}")
    public void updateRelation(@PathVariable String belongId, @RequestBody ForcesLibraryUpdateModel entity) {
        forcesCarryService.update(belongId, entity);
    }


    @PostMapping("modify/carry-list/{belongId}")
    public void updateRelation(@PathVariable String belongId, @RequestBody List<ForcesLibraryUpdateModel> list) {
        forcesCarryService.modifyList(belongId, list);
    }


    @PostMapping("save/carry-list")
    public void saveRelation(@RequestBody List<ForcesCarry> list) {
        forcesCarryService.save(list);
    }


    /**
     * 删除主键兵力
     *
     * @param id
     * @return
     */

    @DeleteMapping(value = "/{id}")
    public int deleteById(@PathVariable String id) {
        return forcesService.deleteById(id);
    }

    /**
     * 删除搭载
     *
     * @param id
     * @return
     */

    @DeleteMapping("{id}/carry/{belongId}")
    public int deleteRelationById(@PathVariable String belongId, @PathVariable String id) {
        if (StringUtils.isBlank(id)) {
            throw new SoulBootException(TinderErrorCode.TINDER_FORCES_ERROR, "id 不能为空");
        }
        if (StringUtils.isBlank(belongId)) {
            throw new SoulBootException(TinderErrorCode.TINDER_FORCES_ERROR, "belongId 不能为空");
        }

        ForcesCarryCondition condition = new ForcesCarryCondition();
        condition.setId(id);
        condition.setBelongId(belongId);
        boolean exists = forcesCarryService.exists(condition);
        if (!exists) {
            throw new SoulBootException(TinderErrorCode.TINDER_FORCES_ERROR, "不存在该兵力");
        }
        return forcesCarryService.deleteById(id);
    }


    /**
     * 根据试验查询兵力和兵力搭载
     *
     * @param experimentId
     * @return
     */

    @GetMapping(value = "/experimentId/{experimentId}")
    public List<Forces> seek(@PathVariable(name = "experimentId") String experimentId) {
        return forcesService.seekByExperiment(experimentId);
    }


    /**
     * 查询兵力搭载
     *
     * @param experimentId
     * @param forcesId
     * @return
     */

    @GetMapping(value = "/list/carry")
    public List<ForcesCarry> getRelation(@RequestParam String experimentId, @RequestParam String forcesId) {
        return forcesCarryService.getByForcesId(forcesId, experimentId);
    }


}
