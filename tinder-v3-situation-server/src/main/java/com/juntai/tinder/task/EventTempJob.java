package com.juntai.tinder.task;

import com.juntai.soulboot.util.JsonUtils;
import com.juntai.tinder.cache.EquipmentCache;
import com.juntai.tinder.config.Constants;
import com.juntai.tinder.config.entity.enums.EventType;
import com.juntai.tinder.entity.Equipment;
import com.juntai.tinder.model.SituationArmyData;
import com.juntai.tinder.scenario.SituationTemArmy;
import com.juntai.tinder.scenario.SituationTemEvent;
import com.juntai.tinder.service.ScenarioEventService;
import com.juntai.tinder.service.SituationRedisManagement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author: 码头工人
 * @Date: 2021/04/06/4:56 下午
 * @Description:
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class EventTempJob {

    @Autowired
    public EquipmentCache equipmentCache;
    @Autowired
    private SituationRedisManagement situationRedisManagement;
    @Autowired
    private ScenarioEventService scenarioEventService;

    @Autowired
    public StringRedisTemplate redisTemplate;

    @Scheduled(fixedDelayString = "200")
    public void eventTempTasks() throws InterruptedException {
        Map<String, List<SituationTemEvent>> hMap = new HashMap<>(16);
        for (Map.Entry<String, SituationTemEvent> tmp : situationRedisManagement.getTemEventDataMap().entrySet()) {
            if (tmp.getValue().getConsumptionTimes() > 10) {
                situationRedisManagement.removeEventTemp(tmp.getValue());
            } else {
                String simId = tmp.getValue().getSimId();
                String instId = tmp.getValue().getInstId();
                Integer type = tmp.getValue().getType();
                //非新增兵力的处理
                if (!EventType.ARMY_ADD.getValue().equals(type) ) {
                    scenarioEventService.scenarioEvent(tmp.getValue());
                    continue;
                }
                String s = (String)redisTemplate.boundHashOps(String.format(Constants.SCENARIO_FORCES, simId)).get(instId);

                if (StringUtils.isBlank(s)) {
                    tmp.getValue().setConsumptionTimes(tmp.getValue().getConsumptionTimes() + 1);
                    continue;
                }
                List<SituationTemEvent> list;
                if (!hMap.containsKey(simId)) {
                    list = new ArrayList<>();
                } else {
                    list = hMap.get(simId);

                }
                list.add(tmp.getValue());
                hMap.put(simId, list);
            }
        }
        for (Map.Entry<String, List<SituationTemEvent>> tmp : hMap.entrySet()) {
            dealAddArmyList(tmp.getValue(), tmp.getKey());
        }

    }

    void dealAddArmyList(List<SituationTemEvent> list, String order) {
        List<SituationArmyData> sendList = new ArrayList<>();
        for (SituationTemEvent situationTemEvent : list) {
            String key = String.format(Constants.SCENARIO_FORCES, order);
            String s = (String)redisTemplate.opsForHash().get(key, situationTemEvent.getInstId());
            if (StringUtils.isBlank(s)) {
                return;
            }
            SituationTemArmy army = JsonUtils.read(s, SituationTemArmy.class);
            SituationArmyData data = new SituationArmyData();
            data.setId(army.getId());
            data.setTeam(army.getTeam());
            data.setName(army.getName());
            data.setType(army.getEquipmentId());
            Equipment byTypeAndTeam = equipmentCache.getCacheData(army.getEquipmentId());
            if (StringUtils.isBlank(byTypeAndTeam.getIconArmy())) {
                data.setIconArmy("军标库_无人艇.svg");
            } else {
                data.setIconArmy(byTypeAndTeam.getIconArmy());
            }
            data.setIcon3dUrl(byTypeAndTeam.getIcon3dUrl());
            situationRedisManagement.removeEventTemp(situationTemEvent);
            sendList.add(data);
        }
        situationRedisManagement.addArmyListData(order, sendList, true);
    }


}
