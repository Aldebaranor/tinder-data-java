package com.juntai.tinder.scenario;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * @Description: 运动特征数据
 * @Author: nemo
 * @Date: 2022/3/23
 */
@Data
public class SituationTemLink implements Serializable {

    private static final long serialVersionUID = -8788432814577448788L;
    private String type;
    private Long src;
    private List<Long> dest;
    private Long time;
}
