package com.juntai.tinder.model;

import com.juntai.soulboot.util.JsonUtils;
import lombok.Data;

import java.io.Serializable;


/**
 * @Description: 情报消息数据
 * @Author: nemo
 * @Date: 2022/3/23
 */
@Data
public class SituationMessageData implements Serializable {

    private static final long serialVersionUID = -8476149978931808624L;
    private String id;
    /**
     * 0 行政区域预警  1 行政区域告警 2文字
     */
    private String type;
    private String instId;

    private String team;

    private String[] content;

    @Override
    public String toString() {

        return String.format("%s@%s@%s@%s@%s", id, type, team, JsonUtils.write(content), instId);
    }


}
