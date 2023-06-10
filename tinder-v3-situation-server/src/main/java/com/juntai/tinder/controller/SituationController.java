package com.juntai.tinder.controller;


import com.juntai.soulboot.common.exception.SoulBootException;
import com.juntai.soulboot.util.JsonUtils;
import com.juntai.soulboot.web.api.ApiResultWrap;
import com.juntai.tinder.config.Constants;
import com.juntai.tinder.config.MetaConfig;
import com.juntai.tinder.config.entity.enums.OperateType;
import com.juntai.tinder.exception.TinderErrorCode;
import com.juntai.tinder.facade.ScenarioFacade;
import com.juntai.tinder.model.*;
import com.juntai.tinder.scenario.ScenarioResult;
import com.juntai.tinder.service.ScenarioEventService;
import com.juntai.tinder.service.SituationRedisManagement;
import com.juntai.tinder.service.impl.EtcdServiceImpl;
import com.juntai.tinder.task.ScheduledTask;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.websocket.server.PathParam;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/3/27
 */

@ApiResultWrap
@RestController
@RequestMapping("/tinder/v3/situation")
@Slf4j
public class SituationController {

    @Autowired
    public SituationRedisManagement situationManagement;

    @Autowired
    private ScheduledTask scheduledTask;

    @Autowired
    private ScenarioEventService scenarioEventService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private MetaConfig metaConfig;
    @Autowired
    private ScenarioFacade scenarioFacade;
    @Autowired
    private EtcdServiceImpl etcdService;



    /**
     * 获取该引擎下的etcd
     * @return
     */
    @GetMapping("/etcd/list")
    public List<EtcdData> getEtcd(){
       return etcdService.getEtcd();

    }

    /**
     * 准备试验，先判断是否改想定是否正在运行，运行则返回simId
     * @param code
     * @return
     */
    @GetMapping("/ready/{code}")
    public String ready(@PathVariable String code) {
        //获取节点当前运行的想定
        List<ResponseRunningData> responseRunningData = queryRunning();
        if(CollectionUtils.isEmpty(responseRunningData)){
            return "new scenario";
        }
        ResponseRunningData data = responseRunningData.stream().filter(q -> StringUtils.equals(code, q.getScenarioCode())).findFirst().orElse(null);
        if (data != null) {
            return data.getSimId();
        }else{
            return "new scenario";
        }
    }

    /**
     * 开始仿真
     * @param scenarioStart
     * @return
     */
    @PostMapping("/start")
    public String start(@RequestBody ScenarioStart scenarioStart) {
        //开始
        String code = scenarioStart.getCode();
        if (StringUtils.isBlank(code)) {
            throw new SoulBootException(TinderErrorCode.SCENARIO_RUNTIME_ERROR,"开始仿真失败,错误信息:想定名为空");
        }
        //获取节点当前运行的想定
        ScenarioModel scenarioModel = new ScenarioModel();
        Distribution distribution = scenarioStart.getDistribution();
        if (distribution == null || CollectionUtils.isEmpty(distribution.getTasks())) {
            throw new SoulBootException(TinderErrorCode.SCENARIO_RUNTIME_ERROR,"任务节点不能为空");
        }
        distribution.setSituation(distribution.getTasks().get(0));
        scenarioModel.setDistribution(distribution);
        Scenario scenario = scenarioFacade.getScenarioByCode(code);
        scenarioModel.setScenario(scenario);
        String url = String.format(Constants.NEW_SCENARIO_URL, code);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> request = new HttpEntity<>(scenarioModel, headers);
        log.info("--------------------------------------------------------------------------------------------------");
        log.info(JsonUtils.write(scenarioModel));
        log.info("--------------------------------------------------------------------------------------------------");
        String taskId ="";
        try {
            ResponseEntity<ScenarioResult> responseGet = restTemplate.postForEntity(metaConfig.getSimulationUrlHead() + url, request, ScenarioResult.class);
            if (responseGet.getStatusCode() != HttpStatus.OK) {
                throw new SoulBootException(TinderErrorCode.SCENARIO_RUNTIME_ERROR,"开始仿真失败,错误信息:" + responseGet.getStatusCode());
            }
            ScenarioResult<String> result = responseGet.getBody();
            if (result.getHasError()) {
                throw new SoulBootException(TinderErrorCode.SCENARIO_RUNTIME_ERROR,"开始仿真失败,错误信息：" + result.getMessage());
            }
            String serialize = JsonUtils.write(result.getResult());
            ResponseOperatorData responseData = JsonUtils.read(serialize, ResponseOperatorData.class);
            taskId = String.valueOf(responseData.getTaskId());
            //记录兵力计划到缓存,供导条使用
            String key = String.format(Constants.SITUATION_PLAN, taskId);
            for (ForcesPlanModel planModel : scenario.getPlan()) {
                redisTemplate.opsForHash().put(key, String.valueOf(planModel.getId()), JsonUtils.write(planModel));
            }

        } catch (Exception ex) {
            throw new SoulBootException(TinderErrorCode.SCENARIO_RUNTIME_ERROR,"开始仿真失败,错误信息:" + ex.getMessage());
        }

        return taskId;


    }

    /**
     * 运行管控操作接口（继续，暂停，加速，减速)
     *
     * @param scenarioOperate
     * @return
     */
    @PostMapping("/operate")
    public String operate(@RequestBody ScenarioOperate scenarioOperate) {
        //开始
        if (scenarioOperate.getType().equals(OperateType.START.getValue())) {
            return null;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> request = new HttpEntity<>(scenarioOperate, headers);
        try {
            ResponseEntity<ScenarioResult> response = restTemplate.postForEntity(metaConfig.getSimulationUrlHead() + Constants.CONTROL_SCENARIO_URL, request, ScenarioResult.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new SoulBootException(TinderErrorCode.SCENARIO_RUNTIME_ERROR,"操作失败,错误信息:" + response.getStatusCode());
            }
            if (scenarioOperate.getType().equals(OperateType.STOP.getValue())) {
                situationManagement.sendScenarioStop(scenarioOperate.getId());
                situationManagement.delete(scenarioOperate.getId());
                String key = String.format(Constants.SITUATION_PLAN, scenarioOperate.getId());
                redisTemplate.delete(key);
            }
            ScenarioResult<String> result = response.getBody();
            if (result.getHasError()) {
                throw new SoulBootException(TinderErrorCode.SCENARIO_RUNTIME_ERROR,"操作失败,错误码：" + result.getMessage());
            }

            return result.getResult();
        } catch (Exception ex) {
            throw new SoulBootException(TinderErrorCode.SCENARIO_RUNTIME_ERROR,"操作失败,错误信息:" + ex.getMessage());
        }

    }

    @PostMapping("/conduct")
    public String conduct(@RequestBody ForcesPlanModel plan)  {
        //开始

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> request = new HttpEntity<>(plan, headers);
        try {
            ResponseEntity<ScenarioResult> response = restTemplate.postForEntity(metaConfig.getSimulationUrlHead() + Constants.CONDUCT_URL, request, ScenarioResult.class);
            System.out.printf("----------conduct------------");
            System.out.printf(JsonUtils.write(request));
            System.out.printf("----------conduct------------");
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new SoulBootException(TinderErrorCode.SCENARIO_RUNTIME_ERROR,"导条失败,错误信息:" + response.getStatusCode());
            }
            ScenarioResult<String> result = response.getBody();
            if (result.getHasError()) {
                throw new SoulBootException(TinderErrorCode.SCENARIO_RUNTIME_ERROR,"导条失败,错误码：" + result.getMessage());
            }
            return result.getResult();
        } catch (Exception ex) {
            throw new SoulBootException(TinderErrorCode.SCENARIO_RUNTIME_ERROR,"导条失败,错误信息:" + ex.getMessage());
        }

    }
    @PostMapping("/incapacity/conduct/{simId}")
    public String conductIncapacityList(@PathVariable String simId,@RequestBody List<String> ids) throws InterruptedException {
        //开始
        for(String id :ids){
            ForcesPlanModel planModel = new ForcesPlanModel();
            planModel.setId(Long.valueOf(simId));
            planModel.setForcesId(Long.valueOf(id));
            planModel.setStartTime(0L);
            planModel.setEndTime(0L);
            planModel.setTaskType("0100");
            planModel.setPoints(new ArrayList<>());
            planModel.setTargets(new ArrayList<>());
            planModel.setAttributes(new ArrayList<>());
            planModel.setPointsId("");
            conduct(planModel);
            Thread.sleep(1);
        }
        return "OK";
    }



    @GetMapping("/event/{simId}")
    public List<SequenceModel> getEvent(@PathVariable String simId) {
        List<SequenceModel> sequenceData = situationManagement.getSequenceData(simId);
        if (CollectionUtils.isEmpty(sequenceData)) {
            return null;
        }
        return sequenceData.stream()
                .sorted(Comparator.comparing(SequenceModel::getTime)).collect(Collectors.toList());
    }


    @PostMapping("/event/{simId}")
    public List<SequenceModel> getEventPost(@PathVariable String simId) {
        List<SequenceModel> sequenceData = situationManagement.getSequenceData(simId);
        if (CollectionUtils.isEmpty(sequenceData)) {
            return null;
        }
        return sequenceData.stream()
                .sorted(Comparator.comparing(SequenceModel::getTime)).collect(Collectors.toList());
    }

    /**
     * 运行过程中查看模型数据
     *
     * @param orderId
     * @param forceId
     * @return
     * @throws InterruptedException
     */

    @GetMapping("/query/model")
    public List<PropertyItem> queryModel(@PathParam(value = "orderId") String orderId, @PathParam(value = "forceId") String forceId)  throws InterruptedException {
        try {
            String url = String.format(Constants.QUERY_MODEL_URL,orderId,forceId);
            ResponseEntity<ScenarioResult> response = restTemplate.getForEntity(metaConfig.getSimulationUrlHead() +url ,  ScenarioResult.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new SoulBootException(TinderErrorCode.SCENARIO_RUNTIME_ERROR,"请求模型失败,错误信息:" + response.getStatusCode());
            }
            ScenarioResult<String> result = response.getBody();
            if (result.getHasError()) {
                throw new SoulBootException(TinderErrorCode.SCENARIO_RUNTIME_ERROR,"请求模型失败,错误码：" + result.getMessage());
            }
            String serialize = JsonUtils.write(result.getResult());
            List<PropertyItem> responseData = JsonUtils.readList(serialize, PropertyItem.class);
            return responseData;
        } catch (Exception ex) {
            throw new SoulBootException(TinderErrorCode.SCENARIO_RUNTIME_ERROR,"请求模型失败,错误信息:" + ex.getMessage());
        }
    }


    @GetMapping("/query/running")
    public List<ResponseRunningData> queryRunning()   {
        List<ResponseRunningData> list =  new ArrayList<>();
        try {
            String url = String.format(Constants.RUNNING_URL);
            ResponseEntity<ScenarioResult> response = restTemplate.getForEntity(metaConfig.getSimulationUrlHead() +url ,  ScenarioResult.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new SoulBootException(TinderErrorCode.SCENARIO_RUNTIME_ERROR,"请求正在运行的想定失败,错误信息:" + response.getStatusCode());
            }
            ScenarioResult<String> result = response.getBody();
            if (result.getHasError()) {
                throw new SoulBootException(TinderErrorCode.SCENARIO_RUNTIME_ERROR,"请求正在运行的想定失败,错误码：" + result.getMessage());
            }
            String serialize = JsonUtils.write(result.getResult());
            list = JsonUtils.readList(serialize, ResponseRunningData.class);
            return list;
        } catch (Exception ex) {
            throw new SoulBootException(TinderErrorCode.SCENARIO_RUNTIME_ERROR,"请求正在运行的想定失败,错误信息:" + ex.getMessage());
        }
    }

//    /**
//     * 心跳
//     * @param order
//     * @param scenario
//     * @return
//     */
//
//    @GetMapping("/heartbeat")
//    public Boolean heartbeat(@RequestParam String order, @RequestParam String scenario) {
//        if(StringUtils.isBlank(order)){
//            return false;
//        }
//        String key = String.format("%s:%s", Constants.SITUATION_HEARTBEAT, order);
//        boolean exists = redisTemplate.hasKey(key);
//        if (!exists) {
//            //生成态势分发线程
//            scheduledTask.addTask(order, 500L);
//            scenarioEventService.add(order, scenario);
//        }
//        redisTemplate.opsForValue().set(key, scenario,10, TimeUnit.SECONDS);
//        return true;
//    }




    @GetMapping("/info/list/{order}")
    public List<String> getSituationInfoData(@PathVariable String order) {
        return situationManagement.getInfoData(order);
    }


    @GetMapping("/army/map/{order}")
    public Map<String, String> getSituationArmyData(@PathVariable String order) {
        Map<String, String> result = new HashMap<>(10);
        Map<String, SituationArmyData> armyDataMap = situationManagement.initArmyDataMap(order);
        for (Map.Entry<String, SituationArmyData> entry : armyDataMap.entrySet()) {
            SituationMoveData moveData = situationManagement.getMoveData(entry.getValue().getId());
            String str = String.format("%s#%s", entry.getValue().toString(), moveData == null ? "" : moveData.toString());
            result.put(entry.getKey(), str);
        }
        return result;
    }


    @GetMapping("/sensor/map/{order}")
    public Map<String, String> getSituationSensorData(@PathVariable String order) {

        Map<String, String> result = new HashMap<>(10);
        Map<String, SituationSensorData> armyDataMap = situationManagement.initSensorDataMap(order);
        for (Map.Entry<String, SituationSensorData> entry : armyDataMap.entrySet()) {
            result.put(entry.getKey(), entry.getValue().toString());
        }
        return result;
    }


    @GetMapping("/link/map/{order}")
    public Map<String, String> getLinkData(@PathVariable String order) {
        Map<String, String> result = new HashMap<>(10);
        Map<String, SituationLinkData> linkMap = situationManagement.initLinkDataMap(order);
        for (Map.Entry<String, SituationLinkData> entry : linkMap.entrySet()) {
            result.put(entry.getKey(), entry.getValue().toString());
        }
        return result;
    }


    @GetMapping("/points/map/{order}")
    public Map<String, String> getPointsData(@PathVariable String order) {
        Map<String, String> result = new HashMap<>(10);
        Map<String, SituationPointsData> pointsMap = situationManagement.initPointsDataMap(order);
        for (Map.Entry<String, SituationPointsData> entry : pointsMap.entrySet()) {
            result.put(entry.getKey(), entry.getValue().toString());
        }
        return result;
    }

    @GetMapping("/message/map/{order}")
    public Map<String, String> getMessageData(@PathVariable String order) {
        Map<String, String> result = new HashMap<>(10);
        Map<String, SituationMessageData> pointsMap = situationManagement.initMessageDataMap(order);
        for (Map.Entry<String, SituationMessageData> entry : pointsMap.entrySet()) {
            result.put(entry.getKey(), entry.getValue().toString());
        }
        return result;
    }

    public int remove(String key) {
        Set<String> keys = redisTemplate.keys(key);
        if (!CollectionUtils.isEmpty(keys)) {
            redisTemplate.delete(new ArrayList<>(keys));
        }
        return keys.size();
    }



}
