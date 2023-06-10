package com.juntai.tinder.facade;


import com.juntai.tinder.entity.RcsData;

import java.util.List;

/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/12/6
 */
public interface RcsDataFacade {

    /**
     * 主键查询
     *
     * @param id 主键
     * @return RcsData
     */

    RcsData getById(String id);

    /**
     * 保存
     *
     * @param entity
     * @return 主键
     */

    String insert(RcsData entity);

    /**
     * 批量删除
     * @param list
     */
    void insertList(List<RcsData> list);

    /**
     * 更新
     *
     * @param entity
     */
    void update(RcsData entity);

    /**
     * 主键删除
     *
     * @param id 主键
     * @return 影响记录行数
     */

    int deleteById(String id);
}
