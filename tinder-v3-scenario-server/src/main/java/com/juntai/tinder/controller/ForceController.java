package com.juntai.tinder.controller;

import com.egova.exception.ExceptionUtils;
import com.egova.web.annotation.Api;
import com.soul.tinder.condition.ForcesCarryCondition;
import com.soul.tinder.condition.ForcesCondition;
import com.soul.tinder.entity.Forces;
import com.soul.tinder.entity.ForcesCarry;
import com.soul.tinder.entity.ForcesLibrary;
import com.soul.tinder.facade.EquipmentTaskFacade;
import com.soul.tinder.model.ForcesEntity;
import com.soul.tinder.model.ForcesLibraryUpdateModel;
import com.soul.tinder.model.ForcesUpdateModel;
import com.soul.tinder.model.Point;
import com.soul.tinder.service.ForcesCarryService;
import com.soul.tinder.service.ForcesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RestController
@RequestMapping("/unity/forces")
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
    @Api
    @GetMapping(value = "/{id}")
    public Forces getById(@PathVariable String id) {
        return forcesService.getById(id);
    }

    /**
     * 根据Id查询搭载详细信息
     * @param id
     * @return
     */
    @Api
    @GetMapping(value = "/{id}/carry")
    public ForcesCarry getCarryById(@PathVariable String id) {
        return forcesCarryService.getById(id);
    }

    @Api
    @PostMapping(value = "/{id}/copy")
    public String copyById(@PathVariable String id, @RequestBody Point point) {
        return forcesService.copyForces(id,null,point);
    }

    @Api
    @GetMapping(value = "/{id}/copy")
    public String copyById(@PathVariable String id) {
        return forcesService.copyForces(id,null,null);
    }



    /**
     * 获取兵力
     *
     * @param experimentId 试验ID
     * @param team         阵营
     * @return 格式与仿真输出的一致
     */
    @Api
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
    @Api
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

    @Api
    @GetMapping(value = "/incapacity/list")
    public List<Forces> getIncapacity(@RequestParam(name = "experimentId") String experimentId) {
        ForcesCondition condition = new ForcesCondition();
        condition.setExperimentId(experimentId);

        List<Forces> list = forcesService.list(condition);
        List<String> equipments = equipmentTaskService.containTask("0100");
        if(CollectionUtils.isEmpty(equipments)){
            return null;
        }
        return list.stream().filter(q -> equipments.contains(q.getEquipmentId())).collect(Collectors.toList());
    }
    @Api
    @GetMapping(value = "/reconnection/list")
    public List<Forces> getReconnection(@RequestParam(name = "experimentId") String experimentId) {
        ForcesCondition condition = new ForcesCondition();
        condition.setExperimentId(experimentId);

        List<Forces> list = forcesService.list(condition);
        List<String> equipments = equipmentTaskService.containTask("0101");
        if(CollectionUtils.isEmpty(equipments)){
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
    @Api
    @PostMapping("/add")
    public String insertFromLibrary(@RequestBody ForcesLibrary entity) {
        return forcesService.addForcesFromLibrary(entity.getExperimentId(), entity.getTeam(), entity);
    }


    /**
     * 新增搭载
     *
     * @param entity
     */
    @Api
    @PostMapping("add/carry/{belongId}")
    public void insertRelation(@PathVariable String belongId, @RequestBody ForcesEntity entity) {
        forcesCarryService.addForcesCarry(entity.getExperimentId(), belongId, entity.getEquipmentId(), entity.getModelId(), entity.getNum());
    }

    /**
     * 修改兵力
     *
     * @param model
     */
    @Api
    @PostMapping(value = "/modify")
    public void update(@RequestBody ForcesUpdateModel model) {
        forcesService.update(model);
    }

    /**
     * 刷新inputInfo
     * @param forceId
     */
    @Api
    @GetMapping(value = "/flash/inputInfo/{forceId}")
    public void flashInput(@PathVariable String forceId) {
        forcesService.flashInput(forceId);
    }


    /**
     * 修改兵力 只更新经纬度高度航向
     * @param model
     */
    @Api
    @PostMapping(value = "/modify/parentId")
    public void updateIgnore(@RequestBody ForcesUpdateModel model) {
        forcesService.updateParentId(model);
    }

    /**
     * 修改搭载
     *
     * @param entity
     */
    @Api
    @PostMapping("/modify/carry/{belongId}")
    public void updateRelation(@PathVariable String belongId, @RequestBody ForcesLibraryUpdateModel entity) {
        forcesCarryService.update(belongId, entity);
    }

    @Api
    @PostMapping("modify/carry-list/{belongId}")
    public void updateRelation(@PathVariable String belongId, @RequestBody List<ForcesLibraryUpdateModel> list) {
        forcesCarryService.modifyList(belongId, list);
    }

    @Api
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
    @Api
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
    @Api
    @DeleteMapping("{id}/carry/{belongId}")
    public int deleteRelationById(@PathVariable String belongId, @PathVariable String id) {
        if (StringUtils.isBlank(id)) {
            throw ExceptionUtils.api("id 不能为空");
        }
        if (StringUtils.isBlank(belongId)) {
            throw ExceptionUtils.api("belongId 不能为空");
        }

        ForcesCarryCondition condition = new ForcesCarryCondition();
        condition.setId(id);
        condition.setBelongId(belongId);
        boolean exists = forcesCarryService.exists(condition);
        if (!exists) {
            throw ExceptionUtils.api("不存在该兵力");
        }
        return forcesCarryService.deleteById(id);
    }


    /**
     * 根据试验查询兵力和兵力搭载
     *
     * @param experimentId
     * @return
     */
    @Api
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
    @Api
    @GetMapping(value = "/list/carry")
    public List<ForcesCarry> getRelation(@RequestParam String experimentId, @RequestParam String forcesId) {
        return forcesCarryService.getByForcesId(forcesId, experimentId);
    }


}
