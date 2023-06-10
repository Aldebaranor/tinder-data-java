package com.juntai.tinder.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName LinkModel
 * @Description
 * @Author ShiZuan
 * @Date 2022/9/19 9:57
 * @Version
 **/
@Data
public class CommunicatesTree implements Serializable {

    /**
     * 兵力Id
     */

    private String forcesId;

    private String forcesName;

    private String parentId;

    private String parentName;

    private String equipmentId;

    private List<CommunicatesTree> children = new ArrayList<>();


}
