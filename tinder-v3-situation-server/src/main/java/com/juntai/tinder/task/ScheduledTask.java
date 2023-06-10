package com.juntai.tinder.task;

import com.juntai.soulboot.util.JsonUtils;
import com.juntai.tinder.config.Constants;
import com.juntai.tinder.model.*;
import com.juntai.tinder.scenario.SituationTemMessage;
import com.juntai.tinder.scenario.SituationTemPoints;
import com.juntai.tinder.scenario.SituationTemRadar;
import com.juntai.tinder.service.SituationRedisManagement;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.FixedDelayTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author: 码头工人
 * @date: 2022/05/07/11:12 下午
 * @description:
 */
@Slf4j
@Component
public class ScheduledTask implements SchedulingConfigurer {

    /**
     * 默认启动10个线程
     */
    private static final Integer DEFAULT_THREAD_POOL = 15;

    private final ConcurrentHashMap<String, ScheduledFuture<?>> scheduledFutures = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, FixedDelayTask> cronTasks = new ConcurrentHashMap<>();


    @Autowired
    public SituationRedisManagement situationRedisManagement;

    @Autowired
    public StringRedisTemplate redisTemplate;

    private volatile ScheduledTaskRegistrar registrar;

    @Override
    public void configureTasks(ScheduledTaskRegistrar registrar) {

        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(DEFAULT_THREAD_POOL,
                new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d").daemon(true).build());
        registrar.setScheduler(executorService);


        this.registrar = registrar;
    }

    @PreDestroy
    public void destroy() {
        this.registrar.destroy();
    }


    public void deleteTask(String taskId) {
        scheduledFutures.keySet().forEach(key -> {
            if (Objects.equals(key, taskId)) {
                scheduledFutures.get(key).cancel(false);
                scheduledFutures.remove(key);
                cronTasks.remove(key);
                return;
            }
        });
    }

    public void addTask(String taskId, Long interval) {

        // 任务表达式为空则跳过
        if (interval == 0) {
            return;
        }
        // 任务已存在并且表达式未发生变化则跳过
        if (scheduledFutures.containsKey(taskId) && cronTasks.get(taskId).getInterval() == interval) {
            return;
        }
        // 任务执行时间发生了变化，则删除该任务
        if (scheduledFutures.containsKey(taskId)) {
            scheduledFutures.get(taskId).cancel(false);
            scheduledFutures.remove(taskId);
            cronTasks.remove(taskId);
        }
        FixedDelayTask task = new FixedDelayTask(new Runnable() {
            @Override
            public void run() {
                // 执行业务逻辑
                try {

                    //每0.2秒发送兵力的移动数据到socket
                    dealMoveData(taskId);
                    //发送雷达和点集的修改
                    dealChangePoints(taskId);
                    dealChangeSensor(taskId);
                    //发送特效的修改
                    dealChangeMessage(taskId);

                } catch (Exception e) {
                    log.error("执行发送消息任务异常，异常信息：{}", e);
                }
            }
        }, interval, interval);

        //注册任务
        registrar.addFixedDelayTask(task);
        ScheduledFuture<?> future = registrar.getScheduler().scheduleWithFixedDelay(task.getRunnable(), task.getInterval());
        cronTasks.put(taskId, task);
        scheduledFutures.put(taskId, future);
    }

    /**
     * 移动数据解析
     *
     * @param split
     * @return
     */
    public MoveData initMoveData(String[] split) {
        MoveData moveData = new MoveData();
        moveData.setLon(Double.valueOf(split[0]));
        moveData.setLat(Double.valueOf(split[1]));
        moveData.setAlt(Double.valueOf(split[2]));
        moveData.setHeading(Double.valueOf(split[3]));
        moveData.setPitch(Double.valueOf(split[4]));
        moveData.setRoll(Double.valueOf(split[5]));
        moveData.setSpeed(Double.valueOf(split[6]));
        moveData.setLife(null);
        return moveData;
    }

    public void dealMoveData(String simId) {
        String key = String.format(Constants.SCENARIO_MOVE, simId);
        Map<Object, Object> moveMap = redisTemplate.opsForHash().entries(key);
        if (CollectionUtils.isEmpty(moveMap)) {
            return;
        }
        String keyDect = String.format(Constants.SCENARIO_MOVE_DECT, simId);
        Map<Object, Object> moveDectMap = redisTemplate.opsForHash().entries(keyDect);
        List<SituationMoveData> list = new ArrayList<>();

        for (Map.Entry<Object, Object> entry : moveMap.entrySet()) {
            String[] split = entry.getValue().toString().split("@");
            if (split.length < 8) {
                log.error(key + "-数据不合法-" + entry.getValue());
            }
            SituationMoveData situationMoveData = new SituationMoveData();
            situationMoveData.setId(simId + "_" + entry.getKey());
            situationMoveData.setMove(initMoveData(split));
            String moveDectString = moveDectMap.get(entry.getKey()).toString();
            if (!StringUtils.isBlank(moveDectString)) {
                String[] splitDect = moveDectString.split("@");
                if (split.length >= 7) {
                    situationMoveData.setMoveDetect(initMoveData(splitDect));
                }
            }
            list.add(situationMoveData);
        }
        situationRedisManagement.sendMoveDataEvent(simId, list);
    }


    public void dealChangePoints(String order) {
        //拼接特效的key
        String pointsKey = String.format(Constants.SCENARIO_POINTS, order);

        Map<Object, Object> pointMap = redisTemplate.opsForHash().entries(pointsKey);
        if (CollectionUtils.isEmpty(pointMap)) {
            return;
        }
        List<SituationPointsData> points = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : pointMap.entrySet()) {
            //添加特效
            SituationTemPoints situationTemPoints = JsonUtils.read(entry.getValue().toString(), SituationTemPoints.class);

            SituationPointsData data = new SituationPointsData();
            data.setId(order + "_" + situationTemPoints.getId());
            data.setArmyId(order + "_" + situationTemPoints.getInstId());
            data.setPoints(situationTemPoints.getData());
            data.setType(String.valueOf(situationTemPoints.getType()));
            points.add(data);
        }
        situationRedisManagement.changePointsDataList(order, points);
    }

    public void dealChangeSensor(String order) {
        //拼接特效的key
        String radarKey = String.format(Constants.SCENARIO_RADAR, order);

        Map<Object, Object> sensorMap = redisTemplate.opsForHash().entries(radarKey);
        if (CollectionUtils.isEmpty(sensorMap)) {
            return;
        }
        List<SituationSensorData> sensorDataList = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : sensorMap.entrySet()) {
            //添加特效
            SituationTemRadar situationTemRadar = JsonUtils.read(entry.getValue().toString(), SituationTemRadar.class);

            SituationSensorData data = new SituationSensorData();
            data.setId(order + "_" + situationTemRadar.getId());
            data.setArmyId(order + "_" + situationTemRadar.getInstId());
            data.setPoints(situationTemRadar.getPoints());
            data.setType(String.valueOf(situationTemRadar.getType()));
            sensorDataList.add(data);
        }
        situationRedisManagement.changeSensorDataList(order, sensorDataList);
    }


    public void dealChangeMessage(String order){
        //拼接特效的key
        String messageKey = String.format(Constants.SCENARIO_MESSAGE, order);

        Map<Object, Object> messageMap = redisTemplate.opsForHash().entries(messageKey);
        if (CollectionUtils.isEmpty(messageMap)) {
            return;
        }
        List<SituationMessageData> messageDataList = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : messageMap.entrySet()) {
            //添加特效
            SituationTemMessage situationTemMessage = JsonUtils.read(entry.getValue().toString(), SituationTemMessage.class);

            SituationMessageData data = new SituationMessageData();
            data.setId(situationTemMessage.getId());
            data.setInstId(situationTemMessage.getInstId());
            data.setTeam(situationTemMessage.getTeam());
            data.setContent(situationTemMessage.getContent());
            data.setType(String.valueOf(situationTemMessage.getType()));
            if( situationTemMessage.getType() ==0 || situationTemMessage.getType() ==1){
                continue;
            }
            messageDataList.add(data);
        }
        situationRedisManagement.changeMessageDataList(order, messageDataList);
    }


}
