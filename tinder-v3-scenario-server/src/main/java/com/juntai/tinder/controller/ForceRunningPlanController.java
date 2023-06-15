package com.juntai.tinder.controller;


import com.juntai.soulboot.common.exception.SoulBootException;
import com.juntai.soulboot.util.JsonUtils;
import com.juntai.soulboot.web.api.ApiResultWrap;
import com.juntai.tinder.config.Constants;
import com.juntai.tinder.config.MetaConfig;
import com.juntai.tinder.entity.ForcesPlan;
import com.juntai.tinder.exception.TinderErrorCode;
import com.juntai.tinder.model.ForcesPlanModel;
import com.juntai.tinder.model.ScenarioResult;
import com.juntai.tinder.service.ForcesPlanService;
import com.juntai.tinder.utils.ConvertUtils;
import com.juntai.tinder.utils.UUIDUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/3/22
 */
@ApiResultWrap
@RestController
@RequestMapping("/tinder/v3/forces-plan/running")
@RequiredArgsConstructor
public class ForceRunningPlanController {

    private final ForcesPlanService forcesPlanService;


    private final RestTemplate restTemplate;
    private final StringRedisTemplate redisTemplate;

    private final MetaConfig metaConfig;

    /**
     * 主键获取
     *
     * @param simId 想定编号
     * @return MapSetting
     */

    @GetMapping(value = "/{id}")
    public ForcesPlan getById(@PathVariable String id, @RequestParam String simId) {
        String key = String.format(Constants.SITUATION_PLAN, simId);
        Map<String, ForcesPlan> hgetall = ConvertUtils.convertMap(redisTemplate.opsForHash().entries(key), ForcesPlan.class);
        return hgetall.get(id);
    }


    @GetMapping(value = "/forces")
    public List<ForcesPlan> queryByForcesId(@RequestParam(name = "simId") String simId,
                                            @RequestParam(name = "forcesId", required = false) String forcesId,
                                            @RequestParam(name = "team", required = false) String team
    ) {

        List<ForcesPlan> plans = new ArrayList<>();
        String key = String.format(Constants.SITUATION_PLAN, simId);
        Map<String, ForcesPlan> hgetall = ConvertUtils.convertMap(redisTemplate.opsForHash().entries(key), ForcesPlan.class);

        hgetall.forEach((k, v) -> {
            boolean flag = true;
            if (!StringUtils.isBlank(forcesId)) {
                if (!StringUtils.equals(forcesId, v.getForcesId())) {
                    flag = false;
                }
            }
            if (!StringUtils.isBlank(team)) {
                if (!StringUtils.equals(team, v.getTeam())) {
                    flag = false;
                }
            }
            if (flag) {
                plans.add(v);
            }
        });
        return plans;

    }


    @PostMapping("/conduct")
    public String conduct(@RequestBody ForcesPlanModel plan) {
        plan.setId(UUIDUtils.getLongUuid());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> request = new HttpEntity<>(plan, headers);
        try {
            ResponseEntity<ScenarioResult> response = restTemplate.postForEntity(metaConfig.getSimulationUrlHead() + Constants.CONDUCT_URL, request, ScenarioResult.class);
            System.out.printf("----------conduct------------");
            System.out.printf(JsonUtils.write(request));
            System.out.printf("----------conduct------------");
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new SoulBootException(TinderErrorCode.SCENARIO_RUNTIME_ERROR, "导条失败,错误信息:" + response.getStatusCode());
            }
            ScenarioResult<String> result = response.getBody();
            if (result.getHasError()) {
                throw new SoulBootException(TinderErrorCode.SCENARIO_RUNTIME_ERROR, "导条失败,错误码：" + result.getMessage());
            }
            //导条成功写入到缓存
            String key = String.format(Constants.SITUATION_PLAN, plan.getSimId());
            redisTemplate.opsForHash().put(key, String.valueOf(plan.getId()), JsonUtils.write(plan));
            return result.getResult();
        } catch (Exception ex) {
            throw new SoulBootException(TinderErrorCode.SCENARIO_RUNTIME_ERROR, "导条失败,错误信息:" + ex.getMessage());
        }

    }


}
