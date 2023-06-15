package com.juntai.tinder.service;

import com.juntai.tinder.entity.MapPoint;
import com.juntai.tinder.facade.MapPointFacade;

/**
 * <p>
 * 地图预置名称 服务类
 * </p>
 *
 * @author nemo
 * @since 2023-06-07
 */
public interface MapPointService extends MapPointFacade {

    /**
     * 根据pageCode查询
     *
     * @param id
     * @return 模型中心点
     */
    int setDisabledById(String id);


    /**
     * 根据想定编号获取
     *
     * @param experimentId
     * @return
     */
    MapPoint getByExperimentId(String experimentId);

    /**
     * 根据实验ID删除
     *
     * @param experimentId
     */
    void deleteByExperimentId(String experimentId);
}
