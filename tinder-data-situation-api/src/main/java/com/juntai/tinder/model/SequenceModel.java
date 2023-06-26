package com.juntai.tinder.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName SequenceModel
 * @Description
 * @Author nemo
 * @Date 2022/4/21 13:15
 * @Version
 **/
@Data
public class SequenceModel implements Serializable {

    private String id;

    private String forceId;

    private String forceName;

    private String time;

    private String endTime;

    private String level;

    private String content;


}
