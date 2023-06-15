package com.juntai.tinder.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName LinkModel
 * @Description
 * @Author ShiZuan
 * @Date 2022/9/19 9:57
 * @Version
 **/
@Data
public class ScenarioCommunicates implements Serializable {

    /**
     * 兵力Id
     */
    private String id;

    /**
     * 兵力名称
     */
    private String name;

    /**
     * 1.空中情报网 2.地面情报网 3.指挥下发网
     */
    private String type;

//    private List<CommunicatesLink> destination;


}
