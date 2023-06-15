package com.juntai.tinder.service;

import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.tinder.condition.ForcesPlanCondition;
import com.juntai.tinder.entity.ForcesPlan;
import com.juntai.tinder.facade.ForcesPlanFacade;
import com.juntai.tinder.model.ForcesPlanModel;

import java.util.List;

/**
 * <p>
 * 1敌我属性 0 红方，1，蓝方，2，白方 服务类
 * </p>
 *
 * @author nemo
 * @since 2023-06-07
 */
public interface ForcesPlanService extends ForcesPlanFacade {

    /**
     * 主键查询
     *
     * @param id 主键
     * @return ExperimentShare
     */

    ForcesPlan getById(String id);

    /**
     * 保存
     *
     * @param entity 兵力计划
     * @return 主键
     */

    String insert(ForcesPlan entity);

    /**
     * 更新插件
     *
     * @param entity
     */
    void insertList(List<ForcesPlan> entity);

    /**
     * 更新
     *
     * @param entity 兵力计划
     */

    void update(ForcesPlan entity);

    /**
     * 主键删除
     *
     * @param id 主键
     * @return 影响记录行数
     */

    int deleteById(String id);

    /**
     * 主键批量删除
     *
     * @param ids 主键
     * @return 影响记录行数
     */
    int deleteByIds(List<String> ids);

    /**
     * 查询所有兵力计划
     *
     * @param condition
     * @return List<Forces>
     */
    List<ForcesPlan> list(ForcesPlanCondition condition);

    /**
     * 分页查询
     *
     * @param model
     * @return
     */
    Pagination<ForcesPlan> page(Query<ForcesPlanCondition, ForcesPlan> model);

    /**
     * 根据试验Id和兵力ID查询兵力计划
     *
     * @param experimentId
     * @param forcesId
     * @param team
     * @return
     */
    List<ForcesPlan> queryByExperiment(String experimentId, String forcesId, String team);


    /**
     * 获取想定中的兵力计划
     *
     * @param experimentId
     * @return
     */
    List<ForcesPlanModel> getPlanModelExperiment(String experimentId);
}
