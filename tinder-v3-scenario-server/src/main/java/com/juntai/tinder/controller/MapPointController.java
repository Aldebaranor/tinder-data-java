package com.juntai.tinder.controller;


import com.juntai.soulboot.web.api.ApiResultWrap;
import com.juntai.tinder.entity.MapPoint;
import com.juntai.tinder.service.MapPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/3/22
 */
@ApiResultWrap
@RestController
@RequestMapping("/tinder/v3/map-point")
@RequiredArgsConstructor
public class MapPointController {

    private final MapPointService mapPointService;

    /**
     * 主键获取
     *
     * @param id 主键
     * @return MapSetting
     */

    @GetMapping(value = "/{id}")
    public MapPoint getById(@PathVariable String id) {
        return mapPointService.getById(id);
    }

    /**
     * 根据想定编码进行获取
     */
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

    @PostMapping
    public String insert(@RequestBody MapPoint mapPoint) {
        return mapPointService.insert(mapPoint);
    }

    /**
     * 更新
     *
     * @param mapPoint 地图设置
     */

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

    @DeleteMapping(value = "/{id}")
    public int deleteById(@PathVariable String id) {
        return mapPointService.deleteById(id);
    }


    /**
     * 设置disabled
     *
     * @param id
     */

    @GetMapping("/disabled")
    public int setDisabledById(@PathVariable String id) {
        return mapPointService.setDisabledById(id);
    }
}
