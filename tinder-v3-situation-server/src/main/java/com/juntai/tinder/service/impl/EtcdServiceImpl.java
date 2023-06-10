package com.juntai.tinder.service.impl;

import com.juntai.soulboot.common.exception.SoulBootException;
import com.juntai.soulboot.util.JsonUtils;
import com.juntai.tinder.config.EtcdClientConfig;
import com.juntai.tinder.config.MetaConfig;
import com.juntai.tinder.exception.TinderErrorCode;
import com.juntai.tinder.model.Distribution;
import com.juntai.tinder.model.EtcdData;
import com.juntai.tinder.model.NodeStatus;
import com.juntai.tinder.service.EtcdService;
import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.options.GetOption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Priority;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @Description: etcd服务$
 * @Author: nemo
 * @Date: 2023/1/6 1:55 PM
 */
@Slf4j
@Service
@Priority(5)
@RequiredArgsConstructor
public class EtcdServiceImpl implements EtcdService {
    private final MetaConfig metaConfig;

    private final EtcdClientConfig etcdClient;


    @Override
    public List<EtcdData> getEtcd() {
        List<EtcdData> list = new ArrayList<>();
        try {
            ByteSequence key = ByteSequence.from(metaConfig.getEtcdKey() + "/online", StandardCharsets.UTF_8);
            GetOption prefixOption = GetOption.newBuilder().isPrefix(true).build();
            CompletableFuture<GetResponse> getFuture = etcdClient.getKv().get(key, prefixOption);
            GetResponse response = getFuture.get();
            response.getKvs().forEach(obj -> {
                String s = obj.getValue().toString(StandardCharsets.UTF_8);
                list.add(JsonUtils.read(s, EtcdData.class));
            });
            return list;
        } catch (Exception ex) {
            throw new SoulBootException(TinderErrorCode.ETCD_ERROR,"获取Etcd数据失败");
        }

    }

    @Override
    public List<NodeStatus> getNodeStatus() {

        List<NodeStatus> list = new ArrayList<>();
        try {
            ByteSequence key = ByteSequence.from(metaConfig.getEtcdKey() + "/status", StandardCharsets.UTF_8);
            GetOption prefixOption = GetOption.newBuilder().isPrefix(true).build();
            CompletableFuture<GetResponse> getFuture = etcdClient.getKv().get(key, prefixOption);
            GetResponse response = getFuture.get();
            response.getKvs().forEach(obj -> {

                String s = obj.getValue().toString(StandardCharsets.UTF_8);
                NodeStatus status = JsonUtils.read(s, NodeStatus.class);
                if (status.getNodeId() != null) {
                    status.setMemory(String.format("%.2f/%.2fGB", status.getMemoryUsed(), status.getMemoryTotal()));
                    status.setDisk(String.format("%.2f/%.2fGB", status.getDiskUsed(), status.getDiskTotal()));
                    list.add(status);
                }

            });
            return list;
        } catch (Exception ex) {
            throw new SoulBootException(TinderErrorCode.ETCD_ERROR,"获取Etcd状态失败");
        }
    }

    @Override
    public Distribution getDistribute(Long situation, Long fusion) {
        Distribution distribution = new Distribution();
        distribution.setTasks(new ArrayList<>());
        distribution.setFusion(fusion == null ? 0L : fusion);
        distribution.setSituation(situation == null ? 0L : situation);
        distribution.setJudge(distribution.getSituation());
        return distribution;
    }
}
