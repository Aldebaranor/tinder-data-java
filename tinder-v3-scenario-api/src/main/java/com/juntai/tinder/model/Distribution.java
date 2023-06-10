package com.juntai.tinder.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 节点分发$
 * @Author: nemo
 * @Date: 2022/12/5 10:15 AM
 */
@Data
public class Distribution implements Serializable {
    private static final long serialVersionUID = 618429195777820423L;

    private List<Long> tasks;

    private Long situation;

    private Long fusion;

    private Long judge;

    private Long repeatTimes;

}
