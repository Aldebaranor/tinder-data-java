package com.juntai.tinder.controller;

import com.juntai.soulboot.web.api.ApiResultWrap;
import com.juntai.tinder.condition.MapGeometryCondition;
import com.juntai.tinder.config.Constants;
import com.juntai.tinder.entity.MapGeometry;
import com.juntai.tinder.model.MapGeometryJson;
import com.juntai.tinder.model.MapGeometryJsonList;
import com.juntai.tinder.service.MapGeometryService;
import com.juntai.tinder.utils.ConvertUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/3/22
 */
@ApiResultWrap
@RestController
@RequestMapping("/tinder/v3/map-geometry")
@RequiredArgsConstructor
public class MapGeometryController {

    private final MapGeometryService mapGeometryService;

    private final StringRedisTemplate redisTemplate;

    /**
     * 主键获取
     *
     * @param id 主键
     * @return MapSetting
     */

    @GetMapping(value = "/{id}")
    public MapGeometry getById(@PathVariable String id) {
        return mapGeometryService.getById(id);
    }


    @PostMapping(value = "/list")
    public List<MapGeometry> list(@RequestBody MapGeometryCondition condition) {
        return mapGeometryService.query(condition);
    }


    @GetMapping(value = "/list")
    public List<MapGeometry> queryByCode(@RequestParam(name = "experimentId") String experimentId,
                                         @RequestParam(name = "team") String team,
                                         @RequestParam(name = "order", required = false) String order

    ) {
        List<MapGeometry> scenarioList = mapGeometryService.getByExperiment(experimentId, team);
        if (StringUtils.isBlank(order)) {
            return scenarioList;
        }
        String key = String.format(Constants.SITUATION_ROUTE, order);
        Map<String, MapGeometry> cacheMap = ConvertUtils.convertMap(redisTemplate.opsForHash().entries(key), MapGeometry.class);

        if (CollectionUtils.isEmpty(cacheMap)) {
            return scenarioList;
        }
        List<MapGeometry> cacheList = cacheMap.values().stream().filter(e -> e.getTeam() == team).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(scenarioList)) {
            return cacheList;
        } else {
            scenarioList.addAll(cacheList);
            return scenarioList;
        }
    }


    @PostMapping("/add")
    public String insert(@RequestBody MapGeometry entity) {
        return mapGeometryService.insert(entity);
    }


    @PostMapping(value = "/json/save")
    public void saveListByJson(@RequestBody MapGeometryJsonList mapGeometryJson) {
        mapGeometryService.updateListByJson(mapGeometryJson.getExperimentId(), mapGeometryJson.getTeam(), mapGeometryJson.getJson());
    }


    @PostMapping(value = "/json/update")
    public void saveByOneJson(@RequestBody MapGeometryJson mapGeometryJson) {
        mapGeometryService.saveOneByJson(mapGeometryJson.getExperimentId(), mapGeometryJson.getTeam(), mapGeometryJson.getJson());
    }


    @DeleteMapping(value = "/{id}")
    public int deleteById(@PathVariable String id) {
        return mapGeometryService.deleteById(id);
    }
}
