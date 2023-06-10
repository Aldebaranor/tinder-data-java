package com.juntai.tinder.service.impl;

import com.egova.json.utils.JsonUtils;
import com.juntai.soulboot.util.JsonUtils;
import com.juntai.tinder.entity.Experiment;
import com.juntai.tinder.entity.Forces;
import com.juntai.tinder.entity.ForcesCarry;
import com.juntai.tinder.model.ForcesPlanModel;
import com.juntai.tinder.model.GeometryModel;
import com.juntai.tinder.model.ModelParameterBase;
import com.juntai.tinder.model.Scenario;
import com.juntai.tinder.service.*;
import com.soul.tinder.entity.CommunicatesPlan;
import com.soul.tinder.entity.Experiment;
import com.soul.tinder.entity.Forces;
import com.soul.tinder.entity.ForcesCarry;
import com.soul.tinder.model.*;
import com.soul.tinder.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Priority;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: $
 * @Author: nemo
 * @Date: 2022/11/4 2:03 PM
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ScenarioServiceImpl implements ScenarioService {

    private final ExperimentService experimentService;
    private final ForcesService forcesService;
    private final MapGeometryService mapGeometryService;
    private final ForcesPlanService forcesPlanService;



    public Scenario getScenario(Experiment experiment) {
        Scenario scenario = new Scenario();
        if (experiment == null) {
            return scenario;
        }
        String experimentId = experiment.getId();
        scenario.setExperiment(experiment);
        List<Forces> forces = forcesService.seekByExperiment(experimentId);
        for (Forces force : forces) {
            force.setAttributeInfo(null);
            List<ModelParameterBase> input = new ArrayList<>();
            if (!StringUtils.isBlank(force.getInputInfo())) {
                input = JsonUtils.readList(force.getInputInfo(), ModelParameterBase.class);
                force.setInputInfo(null);
            }
            force.setInputParameter(input);
            List<ModelParameterBase> output = new ArrayList<>();
            if (!StringUtils.isBlank(force.getOutputInfo())) {
                output = JsonUtils.readList(force.getOutputInfo(), ModelParameterBase.class);
                force.setOutputInfo(null);
            }
            force.setOutputParameter(output);
            for (ForcesCarry relation : force.getRelations()) {
                relation.setAttributeInfo(null);
                List<ModelParameterBase> relationInput = new ArrayList<>();
                if (!StringUtils.isBlank(relation.getInputInfo())) {
                    relationInput = JsonUtils.readList(relation.getInputInfo(), ModelParameterBase.class);
                    relation.setInputParameter(relationInput);
                    relation.setInputInfo(null);
                }

                List<ModelParameterBase> relationOutput = new ArrayList<>();
                if (!StringUtils.isBlank(relation.getOutputInfo())) {
                    relationOutput = JsonUtils.readList(force.getOutputInfo(), ModelParameterBase.class);
                    relation.setOutputParameter(relationOutput);
                    relation.setOutputInfo(null);
                }

            }
        }
        scenario.setForcesInfo(forces);

        List<GeometryModel> geometry = mapGeometryService.getGeometryModelByExperiment(experimentId, null);
        scenario.setGeometry(geometry);

        List<ForcesPlanModel> plans = forcesPlanService.getPlanModelExperiment(experimentId);
        scenario.setPlan(plans);
        return scenario;
    }

    @Override
    public Scenario getScenarioById(String experimentId) {
        Experiment experiment = experimentService.getById(experimentId);
        return getScenario(experiment);
    }

    @Override
    public Scenario getScenarioByIdNoAuth(String experimentId) {
        Experiment experiment = experimentService.getByIdNoAuth(experimentId);
        return getScenario(experiment);
    }

    @Override
    public Scenario getScenarioByCode(String code) {
        Experiment experiment = experimentService.getByScenarioCode(code);
        return getScenario(experiment);
    }

    @Override
    public int getEnemyForces(String code) {
        Experiment experiment = experimentService.getByScenarioCode(code);
        if (experiment == null) {
            return 0;
        }
        String experimentId = experiment.getId();

        List<String> forces = forcesService.queryByExperiment(experimentId,"1");
        return forces.size();
    }




}
