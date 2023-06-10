package com.juntai.tinder.facade;

import com.juntai.tinder.model.Scenario;

/**
 * @Description: $
 * @Author: nemo
 * @Date: 2022/11/4 2:01 PM
 */
public interface ScenarioFacade {

    /**
     * 根据ID获取想定
     *
     * @param experimentId
     * @return
     */
    Scenario getScenarioById(String experimentId);

    /**
     * 根据ID获取想定,不需要验证身份
     *
     * @param experimentId
     * @return
     */
    Scenario getScenarioByIdNoAuth(String experimentId);

    /**
     * 根据想定编号获取想定
     *
     * @param code
     * @return
     */
    Scenario getScenarioByCode(String code);

    /**
     * 获取敌方兵力
     *
     * @param code
     * @return
     */
    int getEnemyForces(String code);


}
