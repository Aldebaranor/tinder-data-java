package com.juntai.tinder.facade;

import com.juntai.tinder.entity.ExperimentTeam;

import java.util.List;

/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/3/22
 */
public interface ExperimentTeamFacade {

    /**
     * 主键查询
     *
     * @param id 主键
     * @return ExperimentShare
     */

    ExperimentTeam getById(String id);

    /**
     * 保存
     *
     * @param experimentTeam 试验分享
     * @return 主键
     */

    String insert(ExperimentTeam experimentTeam);


    /**
     * 批量插入
     *
     * @param list
     */
    void insertList(List<ExperimentTeam> list);

    /**
     * 更新
     *
     * @param experimentTeam 试验分享
     */

    void update(ExperimentTeam experimentTeam);

    /**
     * 一次更新试验下的所有试验成员
     *
     * @param experimentId
     * @param list
     */
    void updateListWithExperimentId(String experimentId, List<ExperimentTeam> list);

    /**
     * 主键删除
     *
     * @param id 主键
     * @return 影响记录行数
     */

    int deleteById(String id);


}
