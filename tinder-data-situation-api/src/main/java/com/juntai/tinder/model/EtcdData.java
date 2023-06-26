package com.juntai.tinder.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName EtcdData
 * @Description
 * @Author nemo
 * @Date 2022/4/21 13:15
 * @Version
 **/
@Data
public class EtcdData implements Serializable {

    private static final long serialVersionUID = 3319068632644685306L;

    private Long nodeId;

    private String hostName;

    private String hostIp;


}
