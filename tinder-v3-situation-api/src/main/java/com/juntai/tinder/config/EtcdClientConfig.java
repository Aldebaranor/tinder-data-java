package com.juntai.tinder.config;

import io.etcd.jetcd.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author Administrator
 * @date 2023/3/14 16:53
 */
@Component
@Configuration
@ConfigurationProperties(prefix = "meta")
@Data
@Slf4j
public class EtcdClientConfig {

    private String etcdUrl;

    private Client client;

    private KV kv;

    private Lock lock;

    private Lease lease;

    private Watch watch;

    private Election election;

    private final String DOUHAO = ",";

    @PostConstruct
    private void init() {
        client = Client.builder().endpoints(etcdUrl).build();
        kv = client.getKVClient();
        lock = client.getLockClient();
        lease = client.getLeaseClient();
        watch = client.getWatchClient();
        election = client.getElectionClient();
        log.info("etcd 已连接： " + etcdUrl);

    }

    @PreDestroy
    private void destroy() {
        election.close();
        kv.close();
        lease.close();
        watch.close();
        lock.close();
        client.close();
    }


}
