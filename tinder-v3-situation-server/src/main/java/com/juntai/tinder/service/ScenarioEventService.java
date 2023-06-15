package com.juntai.tinder.service;

import com.juntai.soulboot.util.JsonUtils;
import com.juntai.tinder.cache.EquipmentCache;
import com.juntai.tinder.config.Constants;
import com.juntai.tinder.config.entity.enums.EventType;
import com.juntai.tinder.entity.Equipment;
import com.juntai.tinder.model.*;
import com.juntai.tinder.scenario.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author: 码头工人
 * @date: 2022/05/10/2:39 下午
 * @description:
 */

@Slf4j
@Service
public class ScenarioEventService {

    private final ConcurrentMap<String, String> orderMap = new ConcurrentHashMap(16);
    @Autowired
    public StringRedisTemplate redisTemplate;
    @Autowired
    private SituationRedisManagement situationRedisManagement;
    @Autowired
    private EquipmentCache equipmentCache;

    public void delete(String order) {
        this.orderMap.remove(order);
    }

    public void add(String order, String scenario) {
        this.orderMap.put(order, scenario);
    }

    public boolean orderStatus(String order) {
        return orderMap.containsKey(order);
    }

    @Async("scenarioEventExecutor")
    public void scenarioEvent(SituationTemEvent situationTemEvent) throws InterruptedException {

        //获取订单号
        String simId = situationTemEvent.getSimId();
        Boolean flag = false;
        //判断该订单是否失效
        if (orderMap.containsKey(simId)) {
            flag = true;
        }
        if (EventType.ARMY_ADD.getValue().equals(situationTemEvent.getType())) {
            dealAddArmy(situationTemEvent, simId, flag);
        } else if (EventType.ARMY_DELETE.getValue().equals(situationTemEvent.getType())) {
            dealDeleteArmy(situationTemEvent, simId, flag, true);
        } else if (EventType.ARMY_OFFLINE.getValue().equals(situationTemEvent.getType())) {
            dealDeleteArmy(situationTemEvent, simId, flag, false);
        } else if (EventType.ARMY_FOUND.getValue().equals(situationTemEvent.getType())) {
            dealChangeArmy(situationTemEvent, simId, flag);
        } else if (EventType.SENSOR_ADD.getValue().equals(situationTemEvent.getType())) {
            dealAddSensor(situationTemEvent, simId, flag);
//        } else if (EventType.SENSOR_CHANGE.getValue().equals(situationTemEvent.getType())) {
//            dealChangeSensor(situationTemEvent, simId, flag);
        } else if (EventType.SENSOR_DELETE.getValue().equals(situationTemEvent.getType())) {
            dealDeleteSensor(situationTemEvent, simId, flag);
        } else if (EventType.LINK_ADD.getValue().equals(situationTemEvent.getType())) {
            dealAddLink(situationTemEvent, simId, flag);
        } else if (EventType.LINK_CHANGE.getValue().equals(situationTemEvent.getType())) {
            dealChangeLink(situationTemEvent, simId, flag);
        } else if (EventType.LINK_DELETE.getValue().equals(situationTemEvent.getType())) {
            dealDeleteLink(situationTemEvent, simId, flag);
        } else if (EventType.POINTS_ADD.getValue().equals(situationTemEvent.getType())) {
            dealAddPoints(situationTemEvent, simId, flag);
//        } else if (EventType.POINTS_CHANGE.getValue().equals(situationTemEvent.getType())) {
//            dealChangePoints(situationTemEvent, simId, flag);
        } else if (EventType.POINTS_DELETE.getValue().equals(situationTemEvent.getType())) {
            dealDeletePoints(situationTemEvent, simId, flag);
        } else if (EventType.MESSAGE_ADD.getValue().equals(situationTemEvent.getType())) {
            dealAddMessage(situationTemEvent, simId, flag);
//        } else if (EventType.MESSAGE_CHANGE.getValue().equals(situationTemEvent.getType())) {
//            dealChangeMessage(situationTemEvent, simId, flag);
        } else if (EventType.MESSAGE_DELETE.getValue().equals(situationTemEvent.getType())) {
            dealDeleteMessage(situationTemEvent, simId, flag);
        }

    }


    public void scenarioInfo(String order, String info) {
        Boolean flag = false;
        if (orderMap.containsKey(order)) {
            flag = true;
        }
        List<String> list = new ArrayList<>();
        list.add(info);
        situationRedisManagement.addInfoData(order, list, flag);

    }

    void dealAddArmy(SituationTemEvent situationTemEvent, String order, Boolean flag) throws InterruptedException {
        //添加兵力新增事件到缓存队列
        if (situationTemEvent.getConsumptionTimes() == 0) {
            situationRedisManagement.addEventTemp(situationTemEvent);
        }
    }


    void dealChangeArmy(SituationTemEvent situationTemEvent, String order, Boolean flag) {
        //发现兵力
        String key = String.format(Constants.SCENARIO_FORCES, order);
        String s = (String) redisTemplate.opsForHash().get(key, situationTemEvent.getInstId());
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
        situationRedisManagement.changeArmyData(order, data, flag);
    }

    void dealDeleteArmy(SituationTemEvent situationTemEvent, String order, Boolean flag, Boolean beDestroy) {
        //兵力毁伤的时候，在两个步长之间多推一次数据
        if (!StringUtils.isBlank(situationTemEvent.getEffectId())) {
            String[] split = situationTemEvent.getEffectId().split("@");
            List<SituationMoveData> list = new ArrayList<>();
            SituationMoveData moveData = new SituationMoveData();
            if (split.length >= 3) {
                moveData.setId(order + "_" + situationTemEvent.getInstId());
                MoveData move = new MoveData();
                move.setLon(Double.valueOf(split[0]));
                move.setLat(Double.valueOf(split[1]));
                move.setAlt(Double.valueOf(split[2]));
                move.setHeading(0D);
                move.setPitch(0D);
                move.setRoll(0D);
                move.setSpeed(0D);
                move.setLife(0D);
                moveData.setMove(move);
            }
            list.add(moveData);
            situationRedisManagement.sendOnlyMoveDataEvent(order, list);
        }
        situationRedisManagement.removeArmyData(order, situationTemEvent.getInstId(), flag, beDestroy);
    }

    void dealAddSensor(SituationTemEvent situationTemEvent, String order, Boolean flag) throws InterruptedException {
        //新数据放在缓存队列
        if (situationTemEvent.getConsumptionTimes() <= 0) {
            log.error("新增雷达发现没有雷达数据，转入缓存处理" + situationTemEvent.getEffectId());
            situationRedisManagement.addEventTemp(situationTemEvent);
            return;
        }
        //拼接特效的key
        String radarKey = String.format(Constants.SCENARIO_RADAR, order);
        //获取指定特效
        String s = (String) redisTemplate.opsForHash().get(radarKey, situationTemEvent.getEffectId());

        if (StringUtils.isBlank(s)) {
            situationRedisManagement.addEventTemp(situationTemEvent);
            return;
        }
        SituationTemRadar situationTemRadar = JsonUtils.read(s, SituationTemRadar.class);
        SituationArmyData situationArmyData = situationRedisManagement.getArmyDataMap(order).get(order + "_" + situationTemRadar.getInstId());
        if (situationArmyData == null) {
            situationRedisManagement.addEventTemp(situationTemEvent);
            log.error("新增雷达发现没有对应兵力数据，转入到缓存中处理" + situationTemEvent.getEffectId());
            return;
        }
        SituationSensorData data = new SituationSensorData();
        data.setId(situationTemRadar.getId());
        data.setArmyId(order + "_" + situationTemRadar.getInstId());
        data.setPoints(situationTemRadar.getPoints());
        data.setType(String.valueOf(situationTemRadar.getType()));
        situationRedisManagement.addSensorData(order, data, flag);
        situationRedisManagement.removeEventTemp(situationTemEvent);
    }

//    void dealChangeSensor(SituationTemEvent situationTemEvent, String order, Boolean flag) {
//        //拼接特效的key
//        String radarKey = String.format(Constants.SCENARIO_RADAR, order);
//        String s = RedisUtils.getService().opsForHash().get(radarKey, situationTemEvent.getEffectId());
//        if (StringUtils.isBlank(s)) {
//            log.error("修改雷达发现没有雷达数据，删除" + situationTemEvent.getEffectId());
//            situationRedisManagement.removeEventTemp(situationTemEvent);
//            return;
//        }
//        //添加特效
//        SituationTemRadar situationTemRadar = JsonUtils.deserialize(s, SituationTemRadar.class);
//        if (situationTemEvent.getTime() > situationTemRadar.getTime()) {
//            situationRedisManagement.addEventTemp(situationTemEvent);
//            log.error("修改雷达数据，发现数据没有更新，转入缓存处理" + situationTemEvent.getEffectId());
//            return;
//        }
//        SituationSensorData data = new SituationSensorData();
//        data.setId(situationTemRadar.getId());
//        data.setArmyId(order + "_" + situationTemRadar.getInstId());
//        data.setPoints(situationTemRadar.getPoints());
//        data.setType(String.valueOf(situationTemRadar.getType()));
//        situationRedisManagement.changeSensorData(order, data, flag);
//    }

    void dealDeleteSensor(SituationTemEvent situationTemEvent, String order, Boolean flag) {
        situationRedisManagement.removeSensorData(order, situationTemEvent.getInstId());
    }

    void dealAddLink(SituationTemEvent situationTemEvent, String order, Boolean flag) throws InterruptedException {
        if (situationTemEvent.getConsumptionTimes() <= 0) {
            log.error("新增链路发现没有链路数据，转入缓存处理" + situationTemEvent.getEffectId());
            situationRedisManagement.addEventTemp(situationTemEvent);
            return;
        }
        //添加链路线
        String key = String.format(Constants.SCENARIO_LINK, order);

        //获取指定特效
        String s = (String) redisTemplate.opsForHash().get(key, situationTemEvent.getEffectId());
        if (StringUtils.isBlank(s)) {
            situationRedisManagement.addEventTemp(situationTemEvent);
            return;
        }

        SituationTemLink link = JsonUtils.read(s, SituationTemLink.class);
        if (link.getSrc() == null || link.getDest() == null) {
            return;
        }

        SituationArmyData situationArmyData = situationRedisManagement.getArmyDataMap(order).get(order + "_" + String.valueOf(link.getSrc()));
        if (situationArmyData == null) {
            situationRedisManagement.addEventTemp(situationTemEvent);
            log.error("新增链路发现发现没有对应兵力数据，转入到缓存中处理" + situationTemEvent.getEffectId());
            return;
        }
        SituationLinkData data = new SituationLinkData();
        data.setId(situationTemEvent.getEffectId());
        data.setStartId(String.valueOf(link.getSrc()));
        List<Long> dest = link.getDest();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dest.size(); i++) {
            sb.append(dest.get(i));
            if (i < dest.size() - 1) {
                sb.append(",");
            }
        }
        data.setEndId(sb.toString());
        data.setType(link.getType());
        situationRedisManagement.addLinkData(order, data);
        situationRedisManagement.removeEventTemp(situationTemEvent);
    }

    void dealChangeLink(SituationTemEvent situationTemEvent, String order, Boolean flag) {
        String key = String.format(Constants.SCENARIO_LINK, order);
        String s = (String) redisTemplate.opsForHash().get(key, situationTemEvent.getEffectId());
        if (StringUtils.isBlank(s)) {
            log.error("修改链路发现没有链路数据，删除" + situationTemEvent.getInstId());
            situationRedisManagement.removeEventTemp(situationTemEvent);
            return;
        }

        SituationTemLink link = JsonUtils.read(s, SituationTemLink.class);
        if (situationTemEvent.getTime() > link.getTime()) {
            situationRedisManagement.addEventTemp(situationTemEvent);
            log.error("修改链路数据，发现数据没有更新，转入缓存处理" + situationTemEvent.getEffectId());
            return;
        }
        if (link.getSrc() == null || link.getDest() == null) {
            return;
        }
        SituationLinkData data = new SituationLinkData();
        data.setId(situationTemEvent.getEffectId());
        data.setStartId(String.valueOf(link.getSrc()));
        List<Long> dest = link.getDest();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dest.size(); i++) {
            sb.append(dest.get(i));
            if (i < dest.size() - 1) {
                sb.append(",");
            }
        }
        data.setEndId(sb.toString());
        data.setType(link.getType());
        situationRedisManagement.changeLinkData(order, data);
    }

    void dealDeleteLink(SituationTemEvent situationTemEvent, String order, Boolean flag) {
        situationRedisManagement.removeLinkData(order, situationTemEvent.getEffectId());
    }

    void dealAddPoints(SituationTemEvent situationTemEvent, String order, Boolean flag) throws InterruptedException {
        if (situationTemEvent.getConsumptionTimes() <= 0) {
            log.error("新增点线发现没有点线数据，转入缓存处理" + situationTemEvent.getEffectId());
            situationRedisManagement.addEventTemp(situationTemEvent);
            return;
        }
        //拼接特效的key
        String pointsKey = String.format(Constants.SCENARIO_POINTS, order);
        //获取指定特效
        String s = (String) redisTemplate.opsForHash().get(pointsKey, situationTemEvent.getEffectId());
        if (StringUtils.isBlank(s)) {
            situationRedisManagement.addEventTemp(situationTemEvent);
            log.error("新增点线发现没有点线数据，转入到缓存中处理" + situationTemEvent.getEffectId());
            return;
        }
        SituationTemPoints situationTemPoints = JsonUtils.read(s, SituationTemPoints.class);
        SituationPointsData data = new SituationPointsData();
        data.setId(situationTemPoints.getId());
        data.setArmyId(order + "_" + situationTemPoints.getInstId());
        data.setPoints(situationTemPoints.getData());
        data.setType(String.valueOf(situationTemPoints.getType()));
        situationRedisManagement.addPointsData(order, data);
        situationRedisManagement.removeEventTemp(situationTemEvent);
    }

//    void dealChangePoints(SituationTemEvent situationTemEvent, String order, Boolean flag) {
//        //拼接特效的key
//        String pointsKey = String.format(Constants.SCENARIO_POINTS, order);
//
//        String s = RedisUtils.getService().opsForHash().get(pointsKey, situationTemEvent.getEffectId());
//        if (StringUtils.isBlank(s)) {
//            log.error("修改点线发现没有点线数据，删除" + situationTemEvent.getEffectId());
//            situationRedisManagement.removeEventTemp(situationTemEvent);
//            return;
//        }
//        //添加特效
//        SituationTemPoints situationTemPoints = JsonUtils.deserialize(s, SituationTemPoints.class);
//        if (situationTemEvent.getTime() > situationTemPoints.getTime()) {
//            situationRedisManagement.addEventTemp(situationTemEvent);
//            log.error("修改点线数据，发现数据没有更新，转入缓存处理" + situationTemEvent.getEffectId());
//            return;
//        }
//        SituationPointsData data = new SituationPointsData();
//        data.setId(situationTemPoints.getId());
//        data.setArmyId(order + "_" + situationTemPoints.getInstId());
//        data.setPoints(situationTemPoints.getData());
//        data.setType(String.valueOf(situationTemPoints.getType()));
//        situationRedisManagement.changePointsData(order, data);
//    }

    void dealDeletePoints(SituationTemEvent situationTemEvent, String order, Boolean flag) {
        situationRedisManagement.removePointsData(order, situationTemEvent.getInstId());
    }

    void dealAddMessage(SituationTemEvent situationTemEvent, String order, Boolean flag) throws InterruptedException {
        if (situationTemEvent.getConsumptionTimes() <= 0) {
            log.error("新增情报发现没有情报数据，转入缓存处理" + situationTemEvent.getEffectId());
            situationRedisManagement.addEventTemp(situationTemEvent);
            return;
        }
        //拼接特效的key
        String messageKey = String.format(Constants.SCENARIO_MESSAGE, order);
        //获取指定特效
        String s = (String) redisTemplate.opsForHash().get(messageKey, situationTemEvent.getEffectId());
        if (StringUtils.isBlank(s)) {
            situationRedisManagement.addEventTemp(situationTemEvent);
            log.error("新增情报发现没有情报数据，转入到缓存中处理" + situationTemEvent.getEffectId());
            return;
        }

        SituationTemMessage situationTemMessage = JsonUtils.read(s, SituationTemMessage.class);
        SituationArmyData situationArmyData = situationRedisManagement.getArmyDataMap(order).get(order + "_" + situationTemMessage.getInstId());
        if (situationArmyData == null) {
            situationRedisManagement.addEventTemp(situationTemEvent);
            log.error("新增情报发现没有对应兵力数据，转入到缓存中处理" + situationTemEvent.getEffectId());
            return;
        }
        SituationMessageData data = new SituationMessageData();
        data.setId(situationTemMessage.getId());
        data.setInstId(situationTemMessage.getInstId());
        data.setTeam(situationTemMessage.getTeam());
        data.setContent(situationTemMessage.getContent());
        data.setType(String.valueOf(situationTemMessage.getType()));
        situationRedisManagement.addMessageData(order, data);
        situationRedisManagement.removeEventTemp(situationTemEvent);
    }

    void dealChangeMessage(SituationTemEvent situationTemEvent, String order, Boolean flag) {
        //拼接特效的key
        String messageKey = String.format(Constants.SCENARIO_MESSAGE, order);

        String s = (String) redisTemplate.opsForHash().get(messageKey, situationTemEvent.getEffectId());
        if (StringUtils.isBlank(s)) {
            log.error("修改情报发现没有情报数据，删除" + situationTemEvent.getEffectId());
            situationRedisManagement.removeEventTemp(situationTemEvent);
            return;
        }
        //添加特效
        SituationTemMessage situationTemMessage = JsonUtils.read(s, SituationTemMessage.class);
        if (situationTemMessage.getType() > 1) {
            return;
        }
        if (situationTemEvent.getTime() > situationTemMessage.getTime()) {
            situationRedisManagement.addEventTemp(situationTemEvent);
            log.error("修改情报数据，发现数据没有更新，转入缓存处理" + situationTemEvent.getEffectId());
            return;
        }
        SituationMessageData data = new SituationMessageData();
        data.setId(situationTemMessage.getId());
        data.setInstId(situationTemMessage.getInstId());
        data.setTeam(situationTemMessage.getTeam());
        data.setContent(situationTemMessage.getContent());
        data.setType(String.valueOf(situationTemMessage.getType()));
        situationRedisManagement.changeMessageData(order, data);
    }

    void dealDeleteMessage(SituationTemEvent situationTemEvent, String order, Boolean flag) {
        situationRedisManagement.removeMessageData(order, situationTemEvent.getEffectId());
    }


}
