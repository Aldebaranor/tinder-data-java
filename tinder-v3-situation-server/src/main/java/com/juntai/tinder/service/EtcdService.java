package com.juntai.tinder.service;

import com.juntai.tinder.model.Distribution;
import com.juntai.tinder.model.EtcdData;
import com.juntai.tinder.model.NodeStatus;

import java.util.List;

/**
 * @Description: 获取etcd数据$
 * @Author: nemo
 * @Date: 2023/1/6 1:54 PM
 */
public interface EtcdService {

    /**
     * 获取ETCD数据
     *
     * @return
     */
    List<EtcdData> getEtcd();

    /**
     * 获取节点状态
     *
     * @return
     */
    List<NodeStatus> getNodeStatus();

    /**
     * 获取多节点的分发信息
     *
     * @param situation
     * @param fusion
     * @return
     */
    Distribution getDistribute(Long situation, Long fusion);
}
