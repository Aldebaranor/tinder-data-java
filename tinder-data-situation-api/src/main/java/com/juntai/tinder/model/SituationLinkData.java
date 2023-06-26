package com.juntai.tinder.model;

import lombok.Data;

import java.io.Serializable;


/**
 * @Description: 态势分发数据
 * @Author: nemo
 * @Date: 2022/3/23
 */
@Data
public class SituationLinkData implements Serializable {

    private static final long serialVersionUID = 5455563917411968475L;

    /**
     * 实体ID
     */
    private String id;
    /**
     * 源端实体ID
     */
    private String startId;

    /**
     * 目的端实体ID
     */
    private String endId;

    /**
     * 链路类型 0跟踪线，1通信 2目标指示 3 指挥网 4 情报网 40人防情报 41 防空情报网
     */
    private String type;


    @Override
    public String toString() {
        return String.format("%s@%s@%s@%s@%s", id, startId, endId, type, "");
    }


}
