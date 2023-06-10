package com.juntai.tinder.controller;

import com.egova.entity.Person;
import com.egova.exception.ExceptionUtils;
import com.egova.facade.PersonFacade;
import com.egova.model.PageResult;
import com.egova.model.QueryModel;
import com.egova.security.UserContext;
import com.egova.web.annotation.Api;
import com.egova.web.annotation.RequestDecorating;
import com.juntai.soulboot.web.api.ApiResultWrap;
import com.juntai.tinder.service.*;
import com.soul.tinder.condition.ExperimentCondition;
import com.soul.tinder.condition.ExperimentTeamCondition;
import com.soul.tinder.entity.Experiment;
import com.soul.tinder.entity.ExperimentTeam;
import com.soul.tinder.entity.MapPoint;
import com.soul.tinder.entity.enums.TeamType;
import com.soul.tinder.model.ExperimentModel;
import com.soul.tinder.model.Scenario;
import com.soul.tinder.service.ExperimentService;
import com.soul.tinder.service.ExperimentTeamService;
import com.soul.tinder.service.MapPointService;
import com.soul.tinder.service.ScenarioService;
import com.soul.tinder.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/5/17
 */
@Slf4j
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

    @Api
    @GetMapping(value = "/{id}/copy")
    public String copyById(@PathVariable String id,@RequestParam String name,@RequestParam String scenarioCode) {
        return experimentService.copyById(id,name,scenarioCode);
    }

    /**
     * 保存
     *
     * @param experimentModel 我的试验
     * @return 主键
     */
    @Api
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
        point.setCreateTime(new Timestamp(System.currentTimeMillis()));
        mapPointService.insert(point);

        List<ExperimentTeam> teamList = new ArrayList<>();
        List<Person> personsBlue = experimentModel.getPersonsBlue();
        List<Person> personsRed = experimentModel.getPersonsRed();
        List<Person> personsWhite = experimentModel.getPersonsWhite();
        List<Person> persons = new ArrayList<>();
        persons.addAll(personsBlue);
        persons.addAll(personsRed);
        persons.addAll(personsWhite);
        long count = persons.stream().distinct().count();
        if (persons.size() > count) {
            //存在重复
            throw ExceptionUtils.api("不同阵营存在重复人员,请重新添加人员!");
        }
        if (!CollectionUtils.isEmpty(personsBlue)) {
            for (Person person : personsBlue) {
                ExperimentTeam experimentTeam = new ExperimentTeam();
                experimentTeam.setExperimentId(id);
                experimentTeam.setTeam(TeamType.BLUE.getValue());
                experimentTeam.setId(UUID.randomUUID().toString());
                experimentTeam.setPersonId(person.getId());
                teamList.add(experimentTeam);
            }
        }
        if (!CollectionUtils.isEmpty(personsRed)) {
            for (Person person : personsRed) {
                ExperimentTeam experimentTeam = new ExperimentTeam();
                experimentTeam.setExperimentId(id);
                experimentTeam.setTeam(TeamType.RED.getValue());
                experimentTeam.setId(UUID.randomUUID().toString());
                experimentTeam.setPersonId(person.getId());
                teamList.add(experimentTeam);
            }
        }
        if (!CollectionUtils.isEmpty(personsWhite)) {
            for (Person person : personsWhite) {
                ExperimentTeam experimentTeam = new ExperimentTeam();
                experimentTeam.setExperimentId(id);
                experimentTeam.setTeam(TeamType.WHITE.getValue());
                experimentTeam.setId(UUID.randomUUID().toString());
                experimentTeam.setPersonId(person.getId());
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
    @Api
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
        point.setCreateTime(new Timestamp(System.currentTimeMillis()));
        point.setLat(experimentModel.getPoint().getLat());
        point.setLon(experimentModel.getPoint().getLon());
        point.setZoom(experimentModel.getPoint().getZoom());
        mapPointService.insert(point);
        //
        List<ExperimentTeam> teamList = new ArrayList<>();
        List<Person> personsBlue = experimentModel.getPersonsBlue();
        List<Person> personsRed = experimentModel.getPersonsRed();
        List<Person> personsWhite = experimentModel.getPersonsWhite();
        List<Person> persons = new ArrayList<>();
        persons.addAll(personsBlue);
        persons.addAll(personsRed);
        persons.addAll(personsWhite);
        long count = persons.stream().distinct().count();
        if (persons.size() > count) {
            //存在重复
            throw ExceptionUtils.api("不同阵营存在重复人员,请重新添加人员!");
        }
        if (!CollectionUtils.isEmpty(personsBlue)) {
            for (Person person : personsBlue) {
                ExperimentTeam experimentTeam = new ExperimentTeam();
                experimentTeam.setExperimentId(experiment.getId());
                experimentTeam.setTeam(TeamType.BLUE.getValue());
                experimentTeam.setId(UUID.randomUUID().toString());
                experimentTeam.setPersonId(person.getId());
                teamList.add(experimentTeam);
            }
        }

        if (!CollectionUtils.isEmpty(personsRed)) {
            for (Person person : personsRed) {
                ExperimentTeam experimentTeam = new ExperimentTeam();
                experimentTeam.setExperimentId(experiment.getId());
                experimentTeam.setTeam(TeamType.RED.getValue());
                experimentTeam.setId(UUID.randomUUID().toString());
                experimentTeam.setPersonId(person.getId());
                teamList.add(experimentTeam);
            }
        }

        if (!CollectionUtils.isEmpty(personsWhite)) {
            for (Person person : personsWhite) {
                ExperimentTeam experimentTeam = new ExperimentTeam();
                experimentTeam.setExperimentId(experiment.getId());
                experimentTeam.setTeam(TeamType.WHITE.getValue());
                experimentTeam.setId(UUID.randomUUID().toString());
                experimentTeam.setPersonId(person.getId());
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
    @Api
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
    @Api
    @PostMapping("/page")
    public PageResult<Experiment> page(@RequestBody QueryModel<ExperimentCondition> model) {
        return experimentService.page(model);
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
        return experimentService.deleteByIds(ids);
    }


    @Api
    @GetMapping("/person/list")
    public List<Person> getPersons() {
        List<Person> all = personService.getAll();
        return all.stream().filter((person -> !person.getCode().equals(UserContext.username()))).collect(Collectors.toList());
    }

    @Api
    @GetMapping("/scenario/view/{experimentId}")
    public Scenario viewFiles(@PathVariable String experimentId) {
        return scenarioService.getScenarioById(experimentId);
    }

    @Api
    @PostMapping("/screenshot/{experimentId}")
    public Map<String, Object> screenshot(@RequestPart("file") MultipartFile multipartFile, @PathVariable String experimentId) {
        Map<String, Object> map = FileUtil.upload(multipartFile);
        Experiment experiment = experimentService.getById(experimentId);
        experiment.setPhoto((String) map.get("id"));
        experimentService.update(experiment);
        return map;
    }

}