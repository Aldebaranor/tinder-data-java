package com.juntai.tinder.service;

import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.tinder.condition.ExperimentCondition;
import com.juntai.tinder.entity.Experiment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.juntai.tinder.facade.ExperimentFacade;

import java.util.List;

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
    Pagination<Experiment> page(Query<ExperimentCondition,Experiment> query);

    /**
     * 主键批量删除
     *
     * @param ids 主键
     * @return 影响记录行数
     */
    int deleteByIds(List<String> ids);

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
