package com.juntai.tinder.service;

import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.tinder.condition.ExperimentCondition;
import com.juntai.tinder.entity.Experiment;
import com.juntai.tinder.facade.ExperimentFacade;

/**
 * <p>
 * 试验类型 服务类
 * </p>
 *
 * @author nemo
 * @since 2023-06-07
 */
public interface ExperimentService extends ExperimentFacade {

    /**
     * 分页查询
     *
     * @param query QueryModel
     * @return 分页数据
     */
    Pagination<Experiment> page(Query<ExperimentCondition, Experiment> query);


    /**
     * 复制想定
     *
     * @param id
     * @param name
     * @param scenarioCode
     * @return
     */
    String copyById(String id, String name, String scenarioCode);

}
