package com.juntai.tinder.service;

import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.tinder.condition.ForcesPlanTemplateCondition;
import com.juntai.tinder.entity.ForcesPlanTemplate;
import com.juntai.tinder.facade.ForcesPlanTemplateFacade;

import java.util.List;

/**
 * <p>
 * 描述 服务类
 * </p>
 *
 * @author nemo
 * @since 2023-06-07
 */
public interface ForcesPlanTemplateService extends ForcesPlanTemplateFacade {

    /**
     * 主键查询
     *
     * @param id 主键
     * @return ForcesPlanTemplate
     */

    ForcesPlanTemplate getById(String id);

    /**
     * 保存
     *
     * @param entity 兵力计划
     * @return 主键
     */

    String insert(ForcesPlanTemplate entity);

    /**
     * 更新插件
     *
     * @param entity
     */
    void insertList(List<ForcesPlanTemplate> entity);

    /**
     * 更新
     *
     * @param entity 兵力计划
     */

    void update(ForcesPlanTemplate entity);

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
    List<ForcesPlanTemplate> list(ForcesPlanTemplateCondition condition);

    /**
     * 分页查询
     *
     * @param model
     * @return
     */
    Pagination<ForcesPlanTemplate> page(Query<ForcesPlanTemplateCondition, ForcesPlanTemplate> model);

    /**
     * 根据试验Id和兵力ID查询兵力计划
     *
     * @param experimentId
     * @param team
     * @return
     */
    List<ForcesPlanTemplate> queryByExperiment(String experimentId, String team);
}
