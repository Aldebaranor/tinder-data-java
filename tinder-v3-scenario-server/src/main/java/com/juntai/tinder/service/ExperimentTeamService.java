package com.juntai.tinder.service;

import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.tinder.condition.ExperimentTeamCondition;
import com.juntai.tinder.entity.ExperimentTeam;
import com.juntai.tinder.facade.ExperimentTeamFacade;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author nemo
 * @since 2023-06-07
 */
public interface ExperimentTeamService extends ExperimentTeamFacade {

    /**
     * 分页查询
     *
     * @param model QueryModel
     * @return 分页数据
     */
    Pagination<ExperimentTeam> page(Query<ExperimentTeamCondition, ExperimentTeam> model);

    /**
     * 主键批量删除
     *
     * @param ids 主键
     * @return 影响记录行数
     */
    int deleteByIds(List<String> ids);

    /**
     * 查询团队成员
     *
     * @param condition
     * @return List<ExperimentTeam>
     */
    List<ExperimentTeam> list(ExperimentTeamCondition condition);


    /**
     * 获取操作人权限
     *
     * @param experimentId
     * @return
     */
    boolean checkAuthorization(String experimentId);

    /**
     * 获取当前登录账号的阵营
     *
     * @param experimentId
     * @return
     */
    String getTeam(String experimentId);
}
