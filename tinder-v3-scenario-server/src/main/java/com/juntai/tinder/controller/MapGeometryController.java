package com.juntai.tinder.controller;

import com.egova.redis.RedisUtils;
import com.egova.web.annotation.Api;
import com.soul.tinder.condition.MapGeometryCondition;
import com.soul.tinder.config.Constants;
import com.soul.tinder.entity.MapGeometry;
import com.soul.tinder.model.MapGeometryJson;
import com.soul.tinder.model.MapGeometryJsonList;
import com.soul.tinder.service.MapGeometryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
@Slf4j
@RestController
@RequestMapping("/unity/map-geometry")
@RequiredArgsConstructor
public class MapGeometryController {

    private final MapGeometryService mapGeometryService;

    /**
     * 主键获取
     *
     * @param id 主键
     * @return MapSetting
     */
    @Api
    @GetMapping(value = "/{id}")
    public MapGeometry getById(@PathVariable String id) {
        return mapGeometryService.getById(id);
    }

    @Api
    @PostMapping(value = "/list")
    public List<MapGeometry> list(@RequestBody MapGeometryCondition condition) {
        return mapGeometryService.query(condition);
    }

    @Api
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
        Map<String, MapGeometry> cacheMap = RedisUtils.getService().extrasForHash().hgetall(key, MapGeometry.class);
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

    @Api
    @PostMapping("/add")
    public String insert(@RequestBody MapGeometry entity) {
        return mapGeometryService.insert(entity);
    }

    @Api
    @PostMapping(value = "/json/save")
    public void saveListByJson(@RequestBody MapGeometryJsonList mapGeometryJson) {
        mapGeometryService.updateListByJson(mapGeometryJson.getExperimentId(), mapGeometryJson.getTeam(), mapGeometryJson.getJson());
    }

    @Api
    @PostMapping(value = "/json/update")
    public void saveByOneJson(@RequestBody MapGeometryJson mapGeometryJson) {
        mapGeometryService.saveOneByJson(mapGeometryJson.getExperimentId(), mapGeometryJson.getTeam(), mapGeometryJson.getJson());
    }


    @Api
    @DeleteMapping(value = "/{id}")
    public int deleteById(@PathVariable String id) {
        return mapGeometryService.deleteById(id);
    }
}
