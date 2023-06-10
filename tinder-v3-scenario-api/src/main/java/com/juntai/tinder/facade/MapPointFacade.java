package com.juntai.tinder.facade;

import com.juntai.tinder.entity.MapPoint;


/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/3/22
 */

public interface MapPointFacade {

    /**
     * 主键查询
     *
     * @param id 主键
     * @return MapPoint
     */

    MapPoint getById(String id);

    /**
     * 保存
     *
     * @param entity 地图中心点
     * @return 主键
     */

    String insert(MapPoint entity);

    /**
     * 更新
     *
     * @param entity 地图中心点
     */
    void update(MapPoint entity);

    /**
     * 保存
     *
     * @param entity
     */
    void save(MapPoint entity);


    /**
     * 主键删除
     *
     * @param id 主键
     * @return 影响记录行数
     */
    int deleteById(String id);

}

