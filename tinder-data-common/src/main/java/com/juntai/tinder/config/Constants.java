package com.juntai.tinder.config;


/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/3/27
 */
public class Constants {

    /**
     * 心跳，用来记录前端发送的请求，内容为订单号
     */
    public static String SITUATION_HEARTBEAT = "situation:heartbeat";

    public static String SITUATION_RUNNING = "situation:running";

    public static String SITUATION_PLAN = "situation:plan:%s";
    public static String SITUATION_SCENARIO = "situation:scenario";
    public static String SITUATION_END_TIME = "situation:end-time";

    /**
     * 项目上效能评估用到的
     */
    public static String SITUATION_EFFICACY = "situation:efficacy";

    public static String SCENARIO_FORCES = "scenario:%s:forces";
    public static String SCENARIO_MOVE = "scenario:%s:move";
    public static String SCENARIO_MOVE_DECT = "scenario:%s:move-dect";

    public static String SCENARIO_RADAR = "scenario:%s:radar";

    public static String SCENARIO_POINTS = "scenario:%s:points";

    public static String SCENARIO_AREA = "scenario:%s:area";
    public static String SCENARIO_LINK = "scenario:%s:link";

    public static String SCENARIO_MESSAGE = "scenario:%s:message";

    /**
     * 导条下发的临时航线
     */
    public static String SITUATION_ROUTE = "situation:%s:route";
    public static String TOPIC_TIME = "TOPIC_TIME";
    public static String TOPIC_REAL_TIME = "TOPIC_REAL_TIME";
    public static String TOPIC_INFO = "TOPIC_INFO";
    public static String TOPIC_MOVE_MAP = "TOPIC_MOVE_MAP";

    public static String TOPIC_ARMY_CHANGE = "TOPIC_ARMY_CHANGE";
    public static String TOPIC_ARMY_OFFLINE = "TOPIC_ARMY_OFFLINE";
    public static String TOPIC_ARMY_ADD = "TOPIC_ARMY_ADD";
    public static String TOPIC_ARMY_DELETE = "TOPIC_ARMY_DELETE";

    public static String TOPIC_SENSOR_CHANGE = "TOPIC_SENSOR_CHANGE";
    public static String TOPIC_SENSOR_ADD = "TOPIC_SENSOR_ADD";
    public static String TOPIC_SENSOR_DELETE = "TOPIC_SENSOR_DELETE";

    public static String TOPIC_LINK_ADD = "TOPIC_LINK_ADD";
    public static String TOPIC_LINK_DELETE = "TOPIC_LINK_DELETE";
    public static String TOPIC_LINK_CHANGE = "TOPIC_LINK_CHANGE";

    public static String TOPIC_POINTS_ADD = "TOPIC_POINTS_ADD";
    public static String TOPIC_POINTS_DELETE = "TOPIC_POINTS_DELETE";
    public static String TOPIC_POINTS_CHANGE = "TOPIC_POINTS_CHANGE";

    public static String TOPIC_MESSAGE_ADD = "TOPIC_MESSAGE_ADD";
    public static String TOPIC_MESSAGE_DELETE = "TOPIC_MESSAGE_DELETE";
    public static String TOPIC_MESSAGE_CHANGE = "TOPIC_MESSAGE_CHANGE";

    /**
     * 停止WebSocket路径
     */
    public static String TOPIC_SCENARIO_STOP = "TOPIC_SCENARIO_STOP";
    /**
     * 新建想定
     */

    public static String NEW_SCENARIO_URL = "/tinder/v1/new?name=%s";
    /**
     * 加减速，停止
     */
    public static String CONTROL_SCENARIO_URL = "/tinder/v1/control";

    /**
     * 查询模型参数
     */
    public static String QUERY_MODEL_URL = "/tinder/v1/query/model?id=%s&forceId=%s";
    /**
     * 导条1.0
     */
    public static String CONDUCT_URL = "/tinder/v1/conduct";
    /**
     * 获取本节点正在运行的想定
     */
    public static String RUNNING_URL = "/tinder/v1/scenario/running";

    public static String RECORD_DATA_URL = "/records/v1/zmq/cache?simId=%s";

    public static String SCENARIO_MONITOR = "/tinder/v1/scenario/monitor";


}