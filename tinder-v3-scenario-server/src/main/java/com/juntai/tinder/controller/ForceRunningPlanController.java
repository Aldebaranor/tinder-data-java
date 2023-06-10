package com.juntai.tinder.controller;

import com.egova.exception.ExceptionUtils;
import com.egova.json.utils.JsonUtils;
import com.egova.redis.RedisUtils;
import com.egova.web.annotation.Api;
import com.soul.tinder.config.Constants;
import com.soul.tinder.config.MetaConfig;
import com.soul.tinder.entity.ForcesPlan;
import com.soul.tinder.model.ForcesPlanModel;
import com.soul.tinder.model.Result;
import com.soul.tinder.service.ForcesPlanService;
import com.soul.tinder.utils.UUIDUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@Slf4j
@RestController
@RequestMapping("/unity/forces-plan/running")
@RequiredArgsConstructor
public class ForceRunningPlanController {

    private final ForcesPlanService forcesPlanService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MetaConfig metaConfig;

    /**
     * 主键获取
     *
     * @param simId 想定编号
     * @return MapSetting
     */
    @Api
    @GetMapping(value = "/{id}")
    public ForcesPlan getById(@PathVariable String id,@RequestParam String simId) {
        String key = String.format(Constants.SITUATION_PLAN,simId);
        Map<String, ForcesPlan> hgetall = RedisUtils.getService().extrasForHash().hgetall(key, ForcesPlan.class);

        return hgetall.get(id);
    }


    @Api
    @GetMapping(value = "/forces")
    public List<ForcesPlan> queryByForcesId(@RequestParam(name = "simId") String simId,
                                            @RequestParam(name = "forcesId", required = false) String forcesId,
                                            @RequestParam(name = "team", required = false) String team
    ) {

        List<ForcesPlan> plans = new ArrayList<>();
        String key = String.format(Constants.SITUATION_PLAN,simId);
        Map<String, ForcesPlan> hgetall = RedisUtils.getService().extrasForHash().hgetall(key, ForcesPlan.class);
        hgetall.forEach((k, v) -> {
            boolean flag = true;
            if(!StringUtils.isBlank(forcesId)){
                if(!StringUtils.equals(forcesId,v.getForcesId())){
                    flag = false;
                }
            }
            if(!StringUtils.isBlank(team)){
                if(!StringUtils.equals(team,v.getTeam())){
                    flag = false;
                }
            }
            if(flag){
                plans.add(v);
            }
        });
        return plans;

    }

    @Api
    @PostMapping("/conduct")
    public String conduct(@RequestBody ForcesPlanModel plan)  {
        plan.setId(UUIDUtils.getLongUuid());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> request = new HttpEntity<>(plan, headers);
        try {
            ResponseEntity<Result> response = restTemplate.postForEntity(metaConfig.getSimulationUrlHead() + Constants.CONDUCT_URL, request, Result.class);
            System.out.printf("----------conduct------------");
            System.out.printf(JsonUtils.serialize(request));
            System.out.printf("----------conduct------------");
            if (response.getStatusCode() != HttpStatus.OK) {
                throw ExceptionUtils.api("导条失败,错误信息:" + response.getStatusCode());
            }
            Result<String> result = response.getBody();
            if (result.getHasError()) {
                throw ExceptionUtils.api("导条失败,错误码：" + result.getMessage());
            }
            //导条成功写入到缓存
            String key = String.format(Constants.SITUATION_PLAN,plan.getSimId());
            RedisUtils.getService().opsForHash().put(key,String.valueOf(plan.getId()),JsonUtils.serialize(plan));
            return result.getResult();
        } catch (Exception ex) {
            throw ExceptionUtils.api("导条失败,错误信息:" + ex.getMessage());
        }

    }




}
