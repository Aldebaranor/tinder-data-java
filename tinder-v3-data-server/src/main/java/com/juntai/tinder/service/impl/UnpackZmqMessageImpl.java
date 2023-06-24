package com.juntai.tinder.service.impl;
import com.juntai.tinder.service.SaveMessage;
import com.juntai.tinder.zeromq.service.SubscriptionListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Priority;

/**
 * @Author: Song
 * @Date 2023/6/21 16:18
 */
@Slf4j
@Priority(10)
@RequiredArgsConstructor
@Component(value = "tinder-zmq")
public class UnpackZmqMessageImpl implements SubscriptionListener {

    @Autowired
    private SaveMessage saveMessage;

    private static final String KEY_EVENT = "4097";
    private static final String KEY_TIME = "4098";
    private static final String KEY_MESSAGE = "4099";

    private static final String MESSAGE_TASK_NEXT_STEP_SYNC = "519";
    private static final String MESSAGE_FUSION_DETECT = "1542";

//    @Autowired
//    private ScenarioEventService scenarioEventService;
//    @Autowired
//    private SituationRedisManagement situationRedisManagement;


    @Override
    public void onReceiveDouble(String s1, byte[] b2) throws InterruptedException {
        //TODO:接收protobuf反序列化

        /**
         * SaveAsCache(SimID,zmq msg);
         */
        saveMessage.SaveAsCache("",null);

//        String[] s = s1.split("_");
//        String order = s[0];
//        String topic = s[1];
//
//        try {
//            if (StringUtils.equals(topic, KEY_EVENT)) {
//                //反序列化
//                TinderProto.ScenarioEvent event;
//                event = TinderProto.ScenarioEvent.parseFrom(b2);
//                SituationTemEvent situationTemEvent = new SituationTemEvent();
//                situationTemEvent.setInstId(event.getInstId());
//                situationTemEvent.setSimId(event.getSimId());
//                situationTemEvent.setTime(event.getTime());
//                situationTemEvent.setType(event.getType());
//                situationTemEvent.setEffectId(event.getEffectId());
//                situationTemEvent.setConsumptionTimes(0);
////                scenarioEventService.scenarioEvent(situationTemEvent);
//            }
//            if (StringUtils.equals(topic, KEY_TIME)) {
//
//                //判断订单是否订阅
////                if (scenarioEventService.orderStatus(order)) {
////                    TinderProto.ScenarioTime time;
////                    time = TinderProto.ScenarioTime.parseFrom(b2);
////                    String message = time.getSimId() + "@" + time.getTime() + "@" + time.getRatio();
////                    situationRedisManagement.sendTimeEvent(time.getSimId(), message);
////                }
//            }
//            if (StringUtils.equals(topic, KEY_MESSAGE)) {
//                TinderProto.ScenarioLog log;
//                log = TinderProto.ScenarioLog.parseFrom(b2);
//
//                long time = log.getTime();
//                String team = log.getTeam();
//                String msg = log.getInfo();
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//                SimpleDateFormat formatterDay = new SimpleDateFormat("HH:mm:ss.SSS");
//                formatter.setTimeZone(TimeZone.getTimeZone("GMT+0"));
//                formatterDay.setTimeZone(TimeZone.getTimeZone("GMT+0"));
//                List<String> list = new ArrayList<>();
//                String format = String.format("%s#%s#%s#%s", order, team, formatterDay.format(new Date(time)), msg);
//                list.add(format);
////                situationRedisManagement.addInfoData(order, list, true);
//
//                if (msg.startsWith("@Time@")) {
//                    msg = msg.replace("@Time@", "");
////                    situationRedisManagement.sendRealTimeEvent(order, msg);
//                }
//
//            }
//
//
//        } catch (InvalidProtocolBufferException e) {
//            e.printStackTrace();
//        }

    }

}
