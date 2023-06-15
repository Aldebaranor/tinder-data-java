package com.juntai.tinder.task;

import com.juntai.soulboot.common.exception.SoulBootException;
import com.juntai.soulboot.util.JsonUtils;
import com.juntai.tinder.config.Constants;
import com.juntai.tinder.config.MetaConfig;
import com.juntai.tinder.exception.TinderErrorCode;
import com.juntai.tinder.model.ScenarioResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


/**
 * @Author: 码头工人
 * @Date: 2021/04/06/4:56 下午
 * @Description:
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ScenarioMonitorJob {

    @Autowired
    public StringRedisTemplate StringRedisTemplate;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private MetaConfig metaConfig;

    @Scheduled(fixedDelayString = "5000")
    public void scenarioStatusTasks() throws InterruptedException {
        try {
            String url = Constants.SCENARIO_MONITOR;
            ResponseEntity<ScenarioResult> response = restTemplate.getForEntity(metaConfig.getSimulationUrlHead() + url, ScenarioResult.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new SoulBootException(TinderErrorCode.SCENARIO_MONITOR_ERROR, "请求想定监控失败,错误信息:" + response.getStatusCode());
            }
            ScenarioResult<String> result = response.getBody();
            if (result.getHasError()) {
                throw new SoulBootException(TinderErrorCode.SCENARIO_MONITOR_ERROR, "请求想定监控失败,错误信息:" + result.getMessage());
            }
            String serialize = JsonUtils.write(result.getResult());
            if (!StringUtils.isBlank(serialize)) {
                StringRedisTemplate.opsForValue().set(Constants.SITUATION_SCENARIO, serialize);
            }
        } catch (Exception ex) {
            throw new SoulBootException(TinderErrorCode.SCENARIO_MONITOR_ERROR, "请求想定监控失败,错误信息:" + ex.getMessage());
        }

    }


}
