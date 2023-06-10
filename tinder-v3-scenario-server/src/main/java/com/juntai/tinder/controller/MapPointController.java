package com.juntai.tinder.controller;

import com.egova.web.annotation.Api;
import com.soul.tinder.entity.MapPoint;
import com.soul.tinder.service.MapPointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/3/22
 */
@Slf4j
@RestController
@RequestMapping("/unity/map-point")
@RequiredArgsConstructor
public class MapPointController {

    private final MapPointService mapPointService;

    /**
     * 主键获取
     *
     * @param id 主键
     * @return MapSetting
     */
    @Api
    @GetMapping(value = "/{id}")
    public MapPoint getById(@PathVariable String id) {
        return mapPointService.getById(id);
    }

    /**
     * 根据想定编码进行获取
     */
    @Api
    @GetMapping(value = "/experimentId/{experimentId}")
    public MapPoint getByExperimentId(@PathVariable String experimentId) {
        return mapPointService.getByExperimentId(experimentId);
    }

    /**
     * 保存
     *
     * @param mapPoint 地图设置
     * @return 主键
     */
    @Api
    @PostMapping
    public String insert(@RequestBody MapPoint mapPoint) {
        return mapPointService.insert(mapPoint);
    }

    /**
     * 更新
     *
     * @param mapPoint 地图设置
     */
    @Api
    @PutMapping
    public void update(@RequestBody MapPoint mapPoint) {
        mapPointService.update(mapPoint);

    }


    /**
     * 主键删除
     *
     * @param id 主键
     * @return 影响记录行数
     */
    @Api
    @DeleteMapping(value = "/{id}")
    public int deleteById(@PathVariable String id) {
        return mapPointService.deleteById(id);
    }

    /**
     * 根据pageCode获取
     *
     * @param pageCode 页面编码
     * @return MapSetting
     */
    @Api
    @GetMapping("/page-code/{pageCode}")
    public List<MapPoint> getByPageCode(@PathVariable String pageCode) {
        return mapPointService.getByPageCode(pageCode);
    }

    /**
     * 设置disabled
     *
     * @param id
     */
    @Api
    @GetMapping("/disabled")
    public Boolean setDisabledById(@PathVariable String id) {
        return mapPointService.setDisabledById(id);
    }
}
