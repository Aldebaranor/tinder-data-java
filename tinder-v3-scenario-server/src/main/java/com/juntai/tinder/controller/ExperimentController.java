package com.juntai.tinder.controller;


import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.soulboot.web.api.ApiResultWrap;
import com.juntai.tinder.condition.ExperimentCondition;
import com.juntai.tinder.condition.ExperimentTeamCondition;
import com.juntai.tinder.condition.UserCondition;
import com.juntai.tinder.entity.Experiment;
import com.juntai.tinder.entity.ExperimentTeam;
import com.juntai.tinder.entity.MapPoint;
import com.juntai.tinder.entity.User;
import com.juntai.tinder.entity.enums.TeamType;
import com.juntai.tinder.model.ExperimentModel;
import com.juntai.tinder.model.Scenario;
import com.juntai.tinder.service.*;
import com.juntai.tinder.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/5/17
 */

@ApiResultWrap
@RestController
@RequestMapping("/tinder/v3/experiment")
@RequiredArgsConstructor
public class ExperimentController {

    private final ExperimentService experimentService;
    private final ExperimentTeamService experimentTeamService;
    private final UserService personService;
    private final MapPointService mapPointService;
    private final ScenarioService scenarioService;


    /**
     * 主键获取
     *
     * @param id 主键
     * @return Experiment
     */

    @GetMapping(value = "/{id}")
    public ExperimentModel getById(@PathVariable String id) {
        ExperimentModel model = new ExperimentModel();
        Experiment experiment = experimentService.getById(id);
        if (experiment == null) {
            return null;
        }
        model.setExperiment(experiment);

        ExperimentTeamCondition condition = new ExperimentTeamCondition();
        condition.setExperimentId(id);
        List<ExperimentTeam> list = experimentTeamService.list(condition);
        for (ExperimentTeam team : list) {
            if (StringUtils.equals(team.getTeam(), TeamType.BLUE.getValue())) {
                model.getPersonsBlue().add(team.getPerson());
            } else if (StringUtils.equals(team.getTeam(), TeamType.RED.getValue())) {
                model.getPersonsRed().add(team.getPerson());
            } else {
                model.getPersonsWhite().add(team.getPerson());
            }
        }
        MapPoint point = mapPointService.getByExperimentId(id);
        model.setPoint(point);
        return model;

    }

    @GetMapping(value = "/{id}/copy")
    public String copyById(@PathVariable String id, @RequestParam String name, @RequestParam String scenarioCode) {
        return experimentService.copyById(id, name, scenarioCode);
    }

    /**
     * 保存
     *
     * @param experimentModel 我的试验
     * @return 主键
     */
    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public String insert(@RequestBody ExperimentModel experimentModel) {
        Experiment experiment = experimentModel.getExperiment();

        if (experiment.getType() == null) {
            experiment.setType(0);
        }

        experiment.valid();
        String id = experimentService.insert(experiment);
        //添加中心原点
        MapPoint point = experimentModel.getPoint();
        point.setName("默认原点");
        point.setExperimentId(id);
        point.setDisabled(false);
        point.setBeDefault(true);
        point.setCreateTime(LocalDateTime.now());
        mapPointService.insert(point);

        List<ExperimentTeam> teamList = new ArrayList<>();
        List<User> personsBlue = experimentModel.getPersonsBlue();
        List<User> personsRed = experimentModel.getPersonsRed();
        List<User> personsWhite = experimentModel.getPersonsWhite();
        List<User> persons = new ArrayList<>();
        persons.addAll(personsBlue);
        persons.addAll(personsRed);
        persons.addAll(personsWhite);

        if (!CollectionUtils.isEmpty(personsBlue)) {
            for (User person : personsBlue) {
                ExperimentTeam experimentTeam = new ExperimentTeam();
                experimentTeam.setExperimentId(id);
                experimentTeam.setTeam(TeamType.BLUE.getValue());
                experimentTeam.setId(UUID.randomUUID().toString());
                experimentTeam.setPersonId(person.getUsername());
                teamList.add(experimentTeam);
            }
        }
        if (!CollectionUtils.isEmpty(personsRed)) {
            for (User person : personsRed) {
                ExperimentTeam experimentTeam = new ExperimentTeam();
                experimentTeam.setExperimentId(id);
                experimentTeam.setTeam(TeamType.RED.getValue());
                experimentTeam.setId(UUID.randomUUID().toString());
                experimentTeam.setPersonId(person.getUsername());
                teamList.add(experimentTeam);
            }
        }
        if (!CollectionUtils.isEmpty(personsWhite)) {
            for (User person : personsWhite) {
                ExperimentTeam experimentTeam = new ExperimentTeam();
                experimentTeam.setExperimentId(id);
                experimentTeam.setTeam(TeamType.WHITE.getValue());
                experimentTeam.setId(UUID.randomUUID().toString());
                experimentTeam.setPersonId(person.getUsername());
                teamList.add(experimentTeam);
            }
        }
        experimentTeamService.insertList(teamList);
        return id;


    }

    /**
     * 更新
     *
     * @param experimentModel 我的试验
     */
    @PutMapping
    @Transactional(rollbackFor = Exception.class)
    public void update(@RequestBody ExperimentModel experimentModel) {
        Experiment experiment = experimentModel.getExperiment();
        experiment.valid();
        if (experiment.getType() == null) {
            experiment.setType(0);
        }
        experimentService.update(experiment);
        mapPointService.deleteByExperimentId(experiment.getId());
        MapPoint point = experimentModel.getPoint();
        point.setName("默认原点");
        point.setExperimentId((experiment.getId()));
        point.setDisabled(false);
        point.setBeDefault(true);
        point.setCreateTime(LocalDateTime.now());
        point.setLat(experimentModel.getPoint().getLat());
        point.setLon(experimentModel.getPoint().getLon());
        point.setZoom(experimentModel.getPoint().getZoom());
        mapPointService.insert(point);
        //
        List<ExperimentTeam> teamList = new ArrayList<>();
        List<User> personsBlue = experimentModel.getPersonsBlue();
        List<User> personsRed = experimentModel.getPersonsRed();
        List<User> personsWhite = experimentModel.getPersonsWhite();
        List<User> persons = new ArrayList<>();
        persons.addAll(personsBlue);
        persons.addAll(personsRed);
        persons.addAll(personsWhite);

        if (!CollectionUtils.isEmpty(personsBlue)) {
            for (User person : personsBlue) {
                ExperimentTeam experimentTeam = new ExperimentTeam();
                experimentTeam.setExperimentId(experiment.getId());
                experimentTeam.setTeam(TeamType.BLUE.getValue());
                experimentTeam.setId(UUID.randomUUID().toString());
                experimentTeam.setPersonId(person.getUsername());
                teamList.add(experimentTeam);
            }
        }

        if (!CollectionUtils.isEmpty(personsRed)) {
            for (User person : personsRed) {
                ExperimentTeam experimentTeam = new ExperimentTeam();
                experimentTeam.setExperimentId(experiment.getId());
                experimentTeam.setTeam(TeamType.RED.getValue());
                experimentTeam.setId(UUID.randomUUID().toString());
                experimentTeam.setPersonId(person.getUsername());
                teamList.add(experimentTeam);
            }
        }

        if (!CollectionUtils.isEmpty(personsWhite)) {
            for (User person : personsWhite) {
                ExperimentTeam experimentTeam = new ExperimentTeam();
                experimentTeam.setExperimentId(experiment.getId());
                experimentTeam.setTeam(TeamType.WHITE.getValue());
                experimentTeam.setId(UUID.randomUUID().toString());
                experimentTeam.setPersonId(person.getUsername());
                teamList.add(experimentTeam);
            }
        }
        experimentTeamService.updateListWithExperimentId(experiment.getId(), teamList);
    }

    /**
     * 主键删除
     *
     * @param id 主键
     * @return 影响记录行数
     */

    @DeleteMapping(value = "/{id}")
    public int deleteById(@PathVariable String id) {
        return experimentService.deleteById(id);
    }

    /**
     * 分页查询
     *
     * @param model 模型
     * @return PageResult
     */

    @PostMapping("/page")
    public Pagination<Experiment> page(@RequestBody Query<ExperimentCondition, Experiment> model) {
        return experimentService.page(model);
    }


    @GetMapping("/person/list")
    public List<User> getPersons() {
        return personService.list(new UserCondition());
    }


    @GetMapping("/scenario/view/{experimentId}")
    public Scenario viewFiles(@PathVariable String experimentId) {
        return scenarioService.getScenarioById(experimentId);
    }


    @PostMapping("/screenshot/{experimentId}")
    public Map<String, Object> screenshot(@RequestPart("file") MultipartFile multipartFile, @PathVariable String experimentId) {
        Map<String, Object> map = FileUtil.upload(multipartFile);
        Experiment experiment = experimentService.getById(experimentId);
        experiment.setPhoto((String) map.get("id"));
        experimentService.update(experiment);
        return map;
    }

}
