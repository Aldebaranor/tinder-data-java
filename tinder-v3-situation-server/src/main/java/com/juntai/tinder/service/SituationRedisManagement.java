package com.juntai.tinder.service;

import com.juntai.soulboot.common.exception.SoulBootException;
import com.juntai.soulboot.util.JsonUtils;
import com.juntai.tinder.cache.EquipmentCache;
import com.juntai.tinder.config.Constants;
import com.juntai.tinder.entity.Equipment;
import com.juntai.tinder.exception.TinderErrorCode;
import com.juntai.tinder.model.*;
import com.juntai.tinder.netty.handler.WebSocketHandler;
import com.juntai.tinder.scenario.*;
import com.juntai.tinder.utils.ConvertUtils;
import com.juntai.tinder.utils.Md5Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Priority;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;


/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/4/1
 */
@Service
@Slf4j
@Priority(5)
public class SituationRedisManagement {

    private static final String CONNECTOR = "_";
    private final ConcurrentMap<String, SituationArmyData> armyData = new ConcurrentHashMap(16);
    private final ConcurrentMap<String, SituationMoveData> moveData = new ConcurrentHashMap(16);
    private final ConcurrentMap<String, SituationSensorData> sensorData = new ConcurrentHashMap(16);
    private final ConcurrentMap<String, SituationLinkData> linkData = new ConcurrentHashMap(16);
    private final ConcurrentMap<String, SituationPointsData> pointsData = new ConcurrentHashMap(16);
    private final ConcurrentMap<String, SituationMessageData> messageData = new ConcurrentHashMap(16);
    private final ConcurrentMap<String, SituationGeometryData> geometryData = new ConcurrentHashMap(16);
    private final ConcurrentMap<String, List<String>> infoData = new ConcurrentHashMap(16);
    private final ConcurrentMap<String, SituationTemEvent> eventTemp = new ConcurrentHashMap(16);
    /**
     * 仿真时序
     */
    private final ConcurrentMap<String, List<SequenceModel>> sequenceData = new ConcurrentHashMap(16);
    @Autowired
    public StringRedisTemplate redisTemplate;
    @Autowired
    public EquipmentCache equipmentCache;
    @Autowired
    private WebSocketHandler webSocketHandler;

    public void delete(String order) {
        armyData.forEach((k, v) -> {
            if (k.startsWith(order + CONNECTOR)) {
                armyData.remove(k);
            }
        });
        moveData.forEach((k, v) -> {
            if (k.startsWith(order + CONNECTOR)) {
                moveData.remove(k);
            }
        });
        sensorData.forEach((k, v) -> {
            if (k.startsWith(order + CONNECTOR)) {
                sensorData.remove(k);
            }
        });
        linkData.forEach((k, v) -> {
            if (k.startsWith(order + CONNECTOR)) {
                linkData.remove(k);
            }
        });
        pointsData.forEach((k, v) -> {
            if (k.startsWith(order + CONNECTOR)) {
                pointsData.remove(k);
            }
        });
        geometryData.forEach((k, v) -> {
            if (k.startsWith(order + CONNECTOR)) {
                geometryData.remove(k);
            }
        });
        messageData.forEach((k, v) -> {
            if (k.startsWith(order + CONNECTOR)) {
                messageData.remove(k);
            }
        });
        infoData.forEach((k, v) -> {
            if (k.startsWith(order)) {
                infoData.remove(k);
            }
        });
        sequenceData.forEach((k, v) -> {
            if (k.startsWith(order)) {
                sequenceData.remove(k);
            }
        });


    }


    //sequence

    public List<SequenceModel> getSequenceData(String order) {
        return sequenceData.get(order);
    }

    public void addSequenceData(String order, SequenceModel sequenceModel) {

        if (CollectionUtils.isEmpty(sequenceData.get(order))) {
            List<SequenceModel> temp = new ArrayList<>();
            temp.add(sequenceModel);
            sequenceData.put(order, temp);
        } else {
            if (sequenceData.get(order).size() > 1000) {
                sequenceData.get(order).remove(0);
            }
            sequenceData.get(order).add(sequenceModel);
        }

    }


    /* message */

    public Map<String, SituationMessageData> initMessageDataMap(String order) {

        Map<String, SituationMessageData> map = new HashMap<>(16);
        String key = String.format(Constants.SCENARIO_MESSAGE, order);


        Map<String, SituationTemMessage> hGetAll = ConvertUtils.convertMap(redisTemplate.opsForHash().entries(key), SituationTemMessage.class);
        for (Map.Entry<String, SituationTemMessage> entry : hGetAll.entrySet()) {
            if (messageData.containsKey(order + CONNECTOR + entry.getValue().getId())) {
                SituationMessageData situationMessageData = messageData.get(order + CONNECTOR + entry.getValue().getId());
                map.put(order + CONNECTOR + entry.getKey(), situationMessageData);

            } else {
                SituationMessageData data = new SituationMessageData();
                data.setId(order + CONNECTOR + entry.getValue().getId());
                data.setTeam(entry.getValue().getTeam());
                data.setContent(entry.getValue().getContent());
                data.setType(String.valueOf(entry.getValue().getType()));
                map.put(data.getId(), data);
                messageData.put(data.getId(), data);
            }

        }
        return map;
    }

    public Map<String, SituationMessageData> getMessageDataMap(String order) {
        Map<String, SituationMessageData> collect = messageData.entrySet().stream()
                .filter(map -> map.getKey().startsWith(order + CONNECTOR))
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
        return collect;

    }

    public void removeMessageData(String order, String id) {
        List<String> message = new ArrayList<>();
        message.add(order + CONNECTOR + id);
        sendDeleteMessageEvent(order, message);
        messageData.remove(order + CONNECTOR + id);
    }

    public void addMessageData(String order, SituationMessageData message) {
        List<SituationMessageData> list = new ArrayList<>();
        message.setId(order + CONNECTOR + message.getId());
        list.add(message);
        sendAddMessageEvent(order, list);
        messageData.put(message.getId(), message);
    }

    public void changeMessageData(String order, SituationMessageData message) {
        List<SituationMessageData> list = new ArrayList<>();
        message.setId(order + CONNECTOR + message.getId());
        list.add(message);
        sendChangeMessageEvent(order, list);
        messageData.put(message.getId(), message);
    }

    public void changeMessageDataList(String order, List<SituationMessageData> message) {

        List<SituationMessageData> list = new ArrayList<>();
        for (SituationMessageData situationMessageData : message) {
            situationMessageData.setId(order + CONNECTOR + situationMessageData.getId());
            list.add(situationMessageData);
            sendChangeMessageEvent(order, list);
            messageData.put(situationMessageData.getId(), situationMessageData);
        }
        sendChangeMessageEvent(order, list);
    }

    //Move

    public SituationMoveData getMoveData(String id) {
        return moveData.get(id);
    }

    public Map<String, SituationMoveData> getMoveDataMap(String order) {

        Map<String, SituationMoveData> collect = moveData.entrySet().stream()
                .filter(map -> map.getKey().startsWith(order + CONNECTOR))
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
        return collect;
    }
    //Info

    public List<String> getInfoData(String order) {
        return infoData.get(order);
    }

    public void addInfoData(String order, List<String> list, Boolean sendFlag) {
        for (String s : list) {
            if (CollectionUtils.isEmpty(infoData.get(order))) {
                List<String> temp = new ArrayList<>();
                temp.add(s);
                infoData.put(order, temp);
            } else {
                if (infoData.get(order).size() > 1000) {
                    infoData.get(order).remove(0);
                }
                infoData.get(order).add(s);
            }

        }
        if (sendFlag) {
            sendInfoEvent(order, list);
        }


    }


    /* Event */
    public Map<String, SituationTemEvent> getTemEventDataMap() {
        return eventTemp;
    }

    public void addEventTemp(SituationTemEvent event) {
        String key = String.format("%s_%s-%s-%s-%s", event.getSimId(), event.getInstId(), event.getType(), event.getEffectId(), event.getTime());
        if (eventTemp.containsKey(key)) {
            event.setConsumptionTimes(event.getConsumptionTimes() + 1);
        } else {
            event.setConsumptionTimes(1);
            eventTemp.put(key, event);
        }
    }

    public void removeEventTemp(SituationTemEvent event) {
        String key = String.format("%s_%s-%s-%s-%s", event.getSimId(), event.getInstId(), event.getType(), event.getEffectId(), event.getTime());
        if (eventTemp.containsKey(key)) {
            eventTemp.remove(key);
        }
    }

    /* Army */
    public Map<String, SituationArmyData> getArmyDataMap(String order) {

        Map<String, SituationArmyData> collect = armyData.entrySet().stream()
                .filter(map -> map.getKey().startsWith(order + CONNECTOR))
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
        return collect;
    }

    public Map<String, SituationArmyData> initArmyDataMap(String order) {

        Map<String, SituationArmyData> map = new HashMap<>(16);
        String key = String.format(Constants.SCENARIO_FORCES, order);
        Map<String, SituationTemArmy> hGetAll = ConvertUtils.convertMap(redisTemplate.opsForHash().entries(key), SituationTemArmy.class);
        for (Map.Entry<String, SituationTemArmy> entry : hGetAll.entrySet()) {
            if (armyData.containsKey(order + CONNECTOR + entry.getValue().getId())) {
                SituationArmyData situationArmyData = armyData.get(order + CONNECTOR + entry.getValue().getId());
                map.put(order + CONNECTOR + entry.getValue().getId(), situationArmyData);

            } else {
                SituationArmyData data = new SituationArmyData();
                data.setId(order + CONNECTOR + entry.getValue().getId());
                data.setTeam(entry.getValue().getTeam());
                data.setName(entry.getValue().getName());
                data.setType(entry.getValue().getEquipmentId());
                Equipment byTypeAndTeam = equipmentCache.getCacheData(entry.getValue().getEquipmentId());
                if (StringUtils.isBlank(byTypeAndTeam.getIconArmy())) {
                    data.setIconArmy("军标库_无人艇.svg");
                } else {
                    data.setIconArmy(byTypeAndTeam.getIconArmy());
                }
                data.setIcon3dUrl(byTypeAndTeam.getIcon3dUrl());

                map.put(order + CONNECTOR + entry.getValue().getId(), data);
                armyData.put(order + CONNECTOR + entry.getValue().getId(), data);
            }

        }

        map.entrySet().stream().sorted(Map.Entry.<String, SituationArmyData>comparingByValue(new Comparator<SituationArmyData>() {
            @Override
            public int compare(SituationArmyData o1, SituationArmyData o2) {
                return o1.getName().compareTo(o2.getName());
            }
        })).forEachOrdered(e -> map.put(e.getKey(), e.getValue()));
        return map;
    }

    public void removeArmyData(String order, String id, Boolean sendFlag, Boolean beDestroy) {
        String keyId = order + CONNECTOR + id;
        //注销该兵力的传感器特效
        Map<String, SituationSensorData> sensorDataMap = getSensorDataMap(order);
        List<SituationSensorData> collect = sensorDataMap.entrySet().stream().filter(map -> id.equals(map.getValue().getArmyId()))
                .map(map -> map.getValue()).collect(Collectors.toList());
        List<String> sensorIds = new ArrayList<>();
        for (SituationSensorData situationSensorData : collect) {
            sensorIds.add(order + CONNECTOR + situationSensorData.getId());
            sensorData.remove(order + CONNECTOR + situationSensorData.getId());
        }
        sendDeleteSensorEvent(order, sensorIds);
        //删除关联的链路
        List<String> deleteList = new ArrayList<>();
        List<SituationLinkData> updateList = new ArrayList<>();
        Map<String, SituationLinkData> linkDataMap = getLinkDataMap(order);
        for (Map.Entry<String, SituationLinkData> tmp : linkDataMap.entrySet()) {
            if (StringUtils.equals(tmp.getValue().getStartId(), keyId)) {
                deleteList.add(tmp.getKey());
                linkData.remove(tmp.getKey());
            } else if (tmp.getValue().getEndId().contains(keyId)) {
                tmp.getValue().getEndId().replace(keyId + ";", "");
                tmp.getValue().getEndId().replace(keyId, "");
                updateList.add(tmp.getValue());
                linkData.put(tmp.getKey(), tmp.getValue());
            }
        }
        sendDeleteLinkEvent(order, deleteList);
        sendChangeLinkEvent(order, updateList);


        List<String> armyList = new ArrayList<>();
        armyList.add(keyId);
        if (sendFlag) {
            if (beDestroy) {
                sendDeleteArmyEvent(order, armyList);
            } else {
                sendOfflineArmyEvent(order, armyList);
            }

        }
        armyData.remove(keyId);
        //移除兵力移动数据
        moveData.remove(keyId);
        log.info("[{}]----->兵力{}被注销", "", keyId);
    }

    public void addArmyData(String order, SituationArmyData data, Boolean sendFlag) {
        List<SituationArmyData> list = new ArrayList<>();
        data.setId(order + CONNECTOR + data.getId());
        list.add(data);
        if (sendFlag) {
            sendAddArmyEvent(order, list);
        }
        armyData.put(data.getId(), data);
    }

    public void addArmyListData(String order, List<SituationArmyData> list, Boolean sendFlag) {
        List<SituationArmyData> result = new ArrayList<>();
        for (SituationArmyData data : list) {
            data.setId(order + CONNECTOR + data.getId());
            result.add(data);
            armyData.put(data.getId(), data);
        }
        if (sendFlag) {
            sendAddArmyEvent(order, result);
        }

    }

    public void changeArmyData(String order, SituationArmyData data, Boolean sendFlag) {
        List<SituationArmyData> list = new ArrayList<>();
        data.setId(order + CONNECTOR + data.getId());
        list.add(data);
        if (sendFlag) {
            sendChangeArmyEvent(order, list);
        }
        armyData.put(data.getId(), data);
    }


    // Sensor

    public Map<String, SituationSensorData> initSensorDataMap(String order) {

        Map<String, SituationSensorData> map = new HashMap<>(16);
        String key = String.format(Constants.SCENARIO_RADAR, order);
        Map<String, SituationTemRadar> hGetAll = ConvertUtils.convertMap(redisTemplate.opsForHash().entries(key), SituationTemRadar.class);
        for (Map.Entry<String, SituationTemRadar> entry : hGetAll.entrySet()) {
            if (sensorData.containsKey(order + CONNECTOR + entry.getValue().getId())) {
                SituationSensorData situationSensorData = sensorData.get(order + CONNECTOR + entry.getValue().getId());
                map.put(order + CONNECTOR + entry.getValue().getId(), situationSensorData);

            } else {
                SituationSensorData data = new SituationSensorData();
                data.setId(order + CONNECTOR + entry.getValue().getId());
                data.setArmyId(entry.getValue().getInstId());
                data.setPoints(entry.getValue().getPoints());
                data.setType(String.valueOf(entry.getValue().getType()));
                map.put(order + CONNECTOR + entry.getValue().getId(), data);
                sensorData.put(order + CONNECTOR + entry.getValue().getId(), data);
            }

        }
        return map;
    }

    public Map<String, SituationSensorData> getSensorDataMap(String order) {
        Map<String, SituationSensorData> collect = sensorData.entrySet().stream()
                .filter(map -> map.getKey().startsWith(order + CONNECTOR))
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
        return collect;
    }

    public void removeSensorData(String order, String id) {
        List<String> sensorIds = new ArrayList<>();
        sensorIds.add(order + CONNECTOR + id);
        sendDeleteSensorEvent(order, sensorIds);
        sensorData.remove(order + CONNECTOR + id);
    }

    public void addSensorData(String order, SituationSensorData data, Boolean sendFlag) {
        String armyId = data.getArmyId();
        if (StringUtils.isBlank(armyId)) {
            throw new SoulBootException(TinderErrorCode.TINDER_COMMON_ERROR, "该传感器的父兵力不能为空");
        }
        List<SituationSensorData> sensors = new ArrayList<>();
        data.setId(order + CONNECTOR + data.getId());
        sensors.add(data);
        if (sendFlag) {
            sendAddSensorEvent(order, sensors);
        }
        sensorData.put(data.getId(), data);
    }

    public void changeSensorData(String order, SituationSensorData data, Boolean sendFlag) {
        List<SituationSensorData> sensors = new ArrayList<>();
        data.setId(order + CONNECTOR + data.getId());

        sensors.add(data);
        if (sendFlag) {
            sendChangeSensorEvent(order, sensors);
        }


        sensorData.put(data.getId(), data);
    }

    public void changeSensorDataList(String order, List<SituationSensorData> data) {
        List<SituationSensorData> result = new ArrayList<>();
        for (SituationSensorData datum : data) {
            if (sensorData.get(datum.getId()) == null) {
                sensorData.put(datum.getId(), datum);
                result.add(datum);
            } else {
                String s = Md5Utils.MD51(datum.toString());
                String s1 = Md5Utils.MD51(sensorData.get(datum.getId()).toString());
                if (!StringUtils.equals(s, s1)) {
                    sensorData.put(datum.getId(), datum);
                    result.add(datum);
                }
            }

        }
        if (!CollectionUtils.isEmpty(result)) {
            sendChangeSensorEvent(order, result);
        }
    }

    //Link

    public Map<String, SituationLinkData> initLinkDataMap(String order) {

        Map<String, SituationLinkData> map = new HashMap<>(16);
        String key = String.format(Constants.SCENARIO_LINK, order);
        Map<String, SituationTemLink> hGetAll = ConvertUtils.convertMap(redisTemplate.opsForHash().entries(key), SituationTemLink.class);
        for (Map.Entry<String, SituationTemLink> entry : hGetAll.entrySet()) {
            if (linkData.containsKey(order + CONNECTOR + entry.getKey())) {
                SituationLinkData situationLinkData = linkData.get(order + CONNECTOR + entry.getKey());
                map.put(order + CONNECTOR + entry.getKey(), situationLinkData);

            } else {
                SituationLinkData data = new SituationLinkData();
                data.setId(order + CONNECTOR + entry.getKey());
                data.setStartId(order + CONNECTOR + entry.getValue().getSrc());
                List<Long> dest = entry.getValue().getDest();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < dest.size(); i++) {
                    sb.append(order + CONNECTOR + dest.get(i));
                    if (i < dest.size() - 1) {
                        sb.append(";");
                    }
                }
                data.setEndId(sb.toString());
                data.setType(String.valueOf(entry.getValue().getType()));
                map.put(data.getId(), data);
                linkData.put(data.getId(), data);
            }

        }
        return map;
    }

    public Map<String, SituationLinkData> getLinkDataMap(String order) {
        Map<String, SituationLinkData> collect = linkData.entrySet().stream()
                .filter(map -> map.getKey().startsWith(order + CONNECTOR))
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
        return collect;
    }

    public void removeLinkData(String order, String id) {
        List<String> links = new ArrayList<>();
        links.add(order + CONNECTOR + id);
        sendDeleteLinkEvent(order, links);
        linkData.remove(order + CONNECTOR + id);
    }

    public void addLinkData(String order, SituationLinkData data) {
        List<SituationLinkData> links = new ArrayList<>();
        data.setId(order + CONNECTOR + data.getId());
        data.setStartId(order + CONNECTOR + data.getStartId());
        String[] split = data.getEndId().split(",");
        StringBuilder end = new StringBuilder();
        if (!StringUtils.isBlank(data.getEndId())) {
            for (int i = 0; i < split.length; i++) {
                end.append(order + CONNECTOR + split[i]);
                if (i < split.length - 1) {
                    end.append(";");
                }
            }
        }
        data.setEndId(end.toString());
        data.setType(String.valueOf(data.getType()));
        links.add(data);
        sendAddLinkEvent(order, links);
        linkData.put(data.getId(), data);
    }

    public void changeLinkData(String order, SituationLinkData data) {
        List<SituationLinkData> links = new ArrayList<>();
        data.setId(order + CONNECTOR + data.getId());
        data.setStartId(order + CONNECTOR + data.getStartId());
        StringBuilder end = new StringBuilder();
        if (!StringUtils.isBlank(data.getEndId())) {
            String[] split = data.getEndId().split(",");
            for (int i = 0; i < split.length; i++) {
                end.append(order + CONNECTOR + split[i]);
                if (i < split.length - 1) {
                    end.append(";");
                }
            }
        }

        data.setEndId(end.toString());
        data.setType(String.valueOf(data.getType()));
        links.add(data);
        sendChangeLinkEvent(order, links);
        linkData.put(data.getId(), data);
    }

    //Points

    public Map<String, SituationPointsData> initPointsDataMap(String order) {

        Map<String, SituationPointsData> map = new HashMap<>(16);
        String key = String.format(Constants.SCENARIO_POINTS, order);
        Map<String, SituationTemPoints> hGetAll = ConvertUtils.convertMap(redisTemplate.opsForHash().entries(key), SituationTemPoints.class);
        for (Map.Entry<String, SituationTemPoints> entry : hGetAll.entrySet()) {
            if (pointsData.containsKey(order + CONNECTOR + entry.getValue().getId())) {
                SituationPointsData situationPointsData = pointsData.get(order + CONNECTOR + entry.getValue().getId());
                map.put(order + CONNECTOR + entry.getKey(), situationPointsData);

            } else {
                SituationPointsData data = new SituationPointsData();
                data.setId(order + CONNECTOR + entry.getValue().getId());
                data.setArmyId(order + "_" + entry.getValue().getInstId());
                data.setPoints(entry.getValue().getData());
                data.setType(String.valueOf(entry.getValue().getType()));
                map.put(data.getId(), data);
                pointsData.put(data.getId(), data);
            }

        }
        return map;
    }

    public Map<String, SituationPointsData> getPointsDataMap(String order) {

        Map<String, SituationPointsData> collect = pointsData.entrySet().stream()
                .filter(map -> map.getKey().startsWith(order + CONNECTOR))
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
        return collect;
    }

    public void removePointsData(String order, String id) {
        List<String> points = new ArrayList<>();
        points.add(order + CONNECTOR + id);
        sendDeletePointsEvent(order, points);
        pointsData.remove(order + CONNECTOR + id);
    }

    public void addPointsData(String order, SituationPointsData data) {
        List<SituationPointsData> points = new ArrayList<>();
        data.setId(order + CONNECTOR + data.getId());
        points.add(data);
        sendAddPointsEvent(order, points);
        pointsData.put(data.getId(), data);
    }

    public void changePointsData(String order, SituationPointsData data) {
        List<SituationPointsData> points = new ArrayList<>();
        data.setId(order + CONNECTOR + data.getId());
        points.add(data);
        sendChangePointsEvent(order, points);
        pointsData.put(data.getId(), data);
    }

    public void changePointsDataList(String order, List<SituationPointsData> data) {

        List<SituationPointsData> result = new ArrayList<>();

        for (SituationPointsData datum : data) {
            if (pointsData.get(datum.getId()) == null) {
                pointsData.put(datum.getId(), datum);
                result.add(datum);
            } else {
                String s = Md5Utils.MD51(datum.toString());
                String s1 = Md5Utils.MD51(pointsData.get(datum.getId()).toString());
                if (!StringUtils.equals(s, s1)) {
                    pointsData.put(datum.getId(), datum);
                    result.add(datum);
                }
            }

        }
        if (!CollectionUtils.isEmpty(result)) {
            sendChangePointsEvent(order, result);
        }

    }

    /* websocket */
    private void sendAddArmyEvent(String simId, List<SituationArmyData> situationData) {
        Map<String, String> map = new HashMap<>(10);
        for (SituationArmyData entity : situationData) {
            map.put(entity.getId(), entity.toString());
        }
        webSocketHandler.sendAllMessage(simId, Constants.TOPIC_ARMY_ADD, JsonUtils.write(map));
        log.info("{}----->{}", simId + "_" + Constants.TOPIC_ARMY_ADD, JsonUtils.write(map));
    }

    private void sendChangeArmyEvent(String simId, List<SituationArmyData> situationData) {
        Map<String, String> map = new HashMap<>(10);
        for (SituationArmyData entity : situationData) {
            map.put(entity.getId(), entity.toString());
        }
        webSocketHandler.sendAllMessage(simId, Constants.TOPIC_ARMY_CHANGE, JsonUtils.write(map));
        log.info("{}----->{}", simId + "_" + Constants.TOPIC_ARMY_CHANGE, JsonUtils.write(map));

    }

    private void sendDeleteArmyEvent(String simId, List<String> armyIds) {
        webSocketHandler.sendAllMessage(simId, Constants.TOPIC_ARMY_DELETE, JsonUtils.write(armyIds));
        log.info("{}----->{}", simId + "_" + Constants.TOPIC_ARMY_DELETE, JsonUtils.write(armyIds));
    }

    private void sendAddSensorEvent(String simId, List<SituationSensorData> situationData) {
        Map<String, String> map = new HashMap<>(10);
        for (SituationSensorData entity : situationData) {
            map.put(entity.getId(), entity.toString());
        }
        webSocketHandler.sendAllMessage(simId, Constants.TOPIC_SENSOR_ADD, JsonUtils.write(map));
        log.info("{}----->{}", simId + "_" + Constants.TOPIC_SENSOR_ADD, JsonUtils.write(map));

    }

    private void sendChangeSensorEvent(String simId, List<SituationSensorData> situationData) {
        Map<String, String> map = new HashMap<>(10);
        for (SituationSensorData entity : situationData) {
            map.put(entity.getId(), entity.toString());
        }
        webSocketHandler.sendAllMessage(simId, Constants.TOPIC_SENSOR_CHANGE, JsonUtils.write(map));
    }

    private void sendDeleteSensorEvent(String simId, List<String> sensorIds) {
        webSocketHandler.sendAllMessage(simId, Constants.TOPIC_SENSOR_DELETE, JsonUtils.write(sensorIds));
        log.info("{}----->{}", simId + "_" + Constants.TOPIC_SENSOR_DELETE, JsonUtils.write(sensorIds));
    }

    public void sendMoveDataEvent(String simId, List<SituationMoveData> situationMoveData) {
        Map<String, String> map = new HashMap<>(10);
        for (SituationMoveData entity : situationMoveData) {
            if (moveData.containsKey(entity.getId())) {
                SituationMoveData temp = moveData.get(entity.getId());
                Long num = temp.getTime();
                entity.setTime(num + 1);
                temp.setTime(num + 1);
                //不移动的兵力，只发二十次,避免创建兵力以后找不到移动数据
                if (!StringUtils.equals(entity.toString(), temp.toString()) || num < 100) {
                    map.put(entity.getId(), entity.toString());
                    moveData.put(entity.getId(), entity);
                }

            } else {
                entity.setTime(0L);
                map.put(entity.getId(), entity.toString());
                moveData.put(entity.getId(), entity);
            }
        }
        if (CollectionUtils.isEmpty(map)) {
            return;
        }
        webSocketHandler.sendAllMessage(simId, Constants.TOPIC_MOVE_MAP, JsonUtils.write(map));

    }

    public void sendOnlyMoveDataEvent(String simId, List<SituationMoveData> situationMoveData) {
        Map<String, String> map = new HashMap<>(10);
        for (SituationMoveData entity : situationMoveData) {
            if (moveData.containsKey(entity.getId())) {
                SituationMoveData temp = moveData.get(entity.getId());
                Long num = temp.getTime();
                entity.setTime(num + 1);
                temp.setTime(num + 1);
                map.put(entity.getId(), entity.toString());
                moveData.put(entity.getId(), entity);

            } else {
                entity.setTime(0L);
                map.put(entity.getId(), entity.toString());
                moveData.put(entity.getId(), entity);
            }
        }
        if (CollectionUtils.isEmpty(map)) {
            return;
        }
        webSocketHandler.sendAllMessage(simId, Constants.TOPIC_MOVE_MAP, JsonUtils.write(map));

    }

    public void sendTimeEvent(String simId, String time) {
        webSocketHandler.sendAllMessage(simId, Constants.TOPIC_TIME, time);

    }

    public void sendRealTimeEvent(String simId, String time) {
        webSocketHandler.sendAllMessage(simId, Constants.TOPIC_REAL_TIME, time);


    }

    private void sendAddLinkEvent(String simId, List<SituationLinkData> situationLinkData) {
        Map<String, String> map = new HashMap<>(10);
        for (SituationLinkData entity : situationLinkData) {
            map.put(entity.getId(), entity.toString());
        }
        webSocketHandler.sendAllMessage(simId, Constants.TOPIC_LINK_ADD, JsonUtils.write(map));
        log.info("{}----->{}", simId + "_" + Constants.TOPIC_LINK_ADD, JsonUtils.write(map));

    }

    private void sendDeleteLinkEvent(String simId, List<String> linkIds) {
        webSocketHandler.sendAllMessage(simId, Constants.TOPIC_LINK_DELETE, JsonUtils.write(linkIds));
        log.info("{}----->{}", simId + "_" + Constants.TOPIC_LINK_DELETE, JsonUtils.write(linkIds));

    }

    private void sendChangeLinkEvent(String simId, List<SituationLinkData> situationLinkData) {

        Map<String, String> map = new HashMap<>(10);
        for (SituationLinkData entity : situationLinkData) {
            map.put(entity.getId(), entity.toString());
        }
        webSocketHandler.sendAllMessage(simId, Constants.TOPIC_LINK_CHANGE, JsonUtils.write(map));
        log.info("{}----->{}", simId + "_" + Constants.TOPIC_LINK_CHANGE, JsonUtils.write(map));

    }

    private void sendAddPointsEvent(String simId, List<SituationPointsData> situationPointsData) {
        Map<String, String> map = new HashMap<>(10);
        for (SituationPointsData entity : situationPointsData) {
            map.put(entity.getId(), entity.toString());
        }
        webSocketHandler.sendAllMessage(simId, Constants.TOPIC_POINTS_ADD, JsonUtils.write(map));
        log.info("{}----->{}", simId + "_" + Constants.TOPIC_POINTS_ADD, JsonUtils.write(map));

    }

    private void sendDeletePointsEvent(String simId, List<String> ids) {
        webSocketHandler.sendAllMessage(simId, Constants.TOPIC_POINTS_DELETE, JsonUtils.write(ids));
    }

    private void sendChangePointsEvent(String simId, List<SituationPointsData> situationPointsData) {
        Map<String, String> map = new HashMap<>(10);
        for (SituationPointsData entity : situationPointsData) {
            map.put(entity.getId(), entity.toString());
        }
        webSocketHandler.sendAllMessage(simId, Constants.TOPIC_POINTS_CHANGE, JsonUtils.write(map));

    }

    private void sendAddMessageEvent(String simId, List<SituationMessageData> situationMessageData) {
        Map<String, String> map = new HashMap<>(10);
        for (SituationMessageData entity : situationMessageData) {
            map.put(entity.getId(), entity.toString());
        }
        webSocketHandler.sendAllMessage(simId, Constants.TOPIC_MESSAGE_ADD, JsonUtils.write(map));
        log.info("{}----->{}", simId + "_" + Constants.TOPIC_MESSAGE_ADD, JsonUtils.write(map));

    }

    private void sendDeleteMessageEvent(String simId, List<String> ids) {
        webSocketHandler.sendAllMessage(simId, Constants.TOPIC_MESSAGE_DELETE, JsonUtils.write(ids));
        log.info("{}----->{}", simId + "_" + Constants.TOPIC_MESSAGE_DELETE, JsonUtils.write(ids));

    }

    private void sendChangeMessageEvent(String simId, List<SituationMessageData> situationMessageData) {
        Map<String, String> map = new HashMap<>(10);
        for (SituationMessageData entity : situationMessageData) {
            map.put(entity.getId(), entity.toString());
        }
        webSocketHandler.sendAllMessage(simId, Constants.TOPIC_MESSAGE_CHANGE, JsonUtils.write(map));

    }

    private void sendInfoEvent(String simId, List<String> info) {

        webSocketHandler.sendAllMessage(simId, Constants.TOPIC_INFO, JsonUtils.write(info));

    }


    public void sendScenarioStop(String simId) {

        webSocketHandler.sendAllMessage(simId, Constants.TOPIC_SCENARIO_STOP, simId);
        log.info("{}----->{}", simId + "_" + Constants.TOPIC_SCENARIO_STOP, simId);

    }

    private void sendOfflineArmyEvent(String simId, List<String> armyList) {

        webSocketHandler.sendAllMessage(simId, Constants.TOPIC_ARMY_OFFLINE, JsonUtils.write(armyList));
    }
}

