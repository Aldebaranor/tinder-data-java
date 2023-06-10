package com.juntai.tinder.scenario;

import lombok.Data;

import java.io.Serializable;


/**
 * @Description: 事件
 * @Author: nemo
 * @Date: 2022/3/23
 */
@Data
public class SituationTemEvent implements Serializable {

    private static final long serialVersionUID = 9175559009927740435L;
    private Long time;
    /**
     * 特效id
     */
    private String effectId;
    /**
     * 订单号
     */
    private String simId;
    /**
     * 兵力实体id
     */
    private String instId;
    /**
     * 10 兵力添加，11兵力销毁，12.兵力下线,13兵力被发现
     * 20.传感器生成，21传感器关，22传感器变化），
     * 30.情报生成，31.情报销毁，32情报变化
     * 40.区域生成，41区域销毁 42区域变化
     * 50.数据链路生成，51数据链路销毁 52数据链路变化
     * 60.点线生成，61点线销毁 62点线变化
     */
    private Integer type;
    /**
     * 事件被尝试消费的次数，五次尝试不成功则过滤该消息
     */
    private Integer consumptionTimes;


}
