package com.juntai.tinder.facade;

import com.juntai.tinder.condition.ExperimentCondition;
import com.juntai.tinder.entity.Experiment;

import java.util.List;

/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/3/22
 */
public interface ExperimentFacade {

    /**
     * 主键查询
     *
     * @param id 主键
     * @return Experiment
     */
    Experiment getById(String id);

    /**
     * 逐渐查询，不需要验证身份
     *
     * @param id
     * @return
     */
    Experiment getByIdNoAuth(String id);

    /**
     * 获取所有，包括兵力数量
     *
     * @return
     */
    List<Experiment> all();

    /**
     * 根据想定编号查询
     *
     * @param scenarioCode
     * @return
     */
    Experiment getByScenarioCode(String scenarioCode);

    /**
     * 保存
     *
     * @param experiment 我的试验
     * @return 主键
     */

    String insert(Experiment experiment);

    /**
     * 更新
     *
     * @param experiment 我的试验
     */

    void update(Experiment experiment);

    /**
     * 主键删除
     *
     * @param id 主键
     * @return 影响记录行数
     */

    int deleteById(String id);

    /**
     * 获取总数
     *
     * @param condition
     * @return
     */
    Long count(ExperimentCondition condition);

}
