package com.juntai.tinder.service;

import com.juntai.tinder.entity.Equipment;
import com.juntai.tinder.entity.Forces;
import com.baomidou.mybatisplus.extension.service.IService;
import com.juntai.tinder.entity.ForcesLibrary;
import com.juntai.tinder.facade.ForcesFacade;
import com.juntai.tinder.model.Point;

import java.util.List;

/**
 * <p>
 * 搭载母体 服务类
 * </p>
 *
 * @author nemo
 * @since 2023-06-07
 */
public interface ForcesService extends ForcesFacade {
    /**
     * 复制兵力
     *
     * @param id
     * @param experimentId
     * @param point
     * @return
     */
    String copyForces(String id , String experimentId, Point point);

    /**
     * 用于复制想定中的兵力复制
     * @param id
     * @param experimentId
     * @return
     */
    String copyById(String id, String experimentId);
    /**
     * 主键查询
     *
     * @param id 主键
     * @return ExperimentShare
     */

    Forces getById(String id);

    /**
     * 保存
     *
     * @param entity 兵力
     * @return 主键
     */

    String insert(Forces entity);

    /**
     * 批量插入
     *
     * @param entity
     */
    void insertList(List<Forces> entity);

    /**
     * 添加兵力
     *
     * @param experimentId
     * @param team
     * @param equipment
     * @param modelId      模型编号 模型->装备
     * @return
     */
    String addForces(String experimentId, String team, Equipment equipment, String modelId);


    /**
     * 从兵力资源库添加兵力
     *
     * @param experimentId
     * @param team
     * @param equipment
     * @return
     */
    String addForcesFromLibrary(String experimentId, String team, ForcesLibrary equipment);

    /**
     * 更新
     *
     * @param entity 兵力
     */

    void update(ForcesUpdateModel entity);

    void updateParentId(ForcesUpdateModel entity);

    /**
     * 主键删除
     *
     * @param id 主键
     * @return 影响记录行数
     */

    int deleteById(String id);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    int deleteByIds(List<String> ids);

    /**
     * 查询所有兵力
     *
     * @param condition
     * @return List<Forces>
     */
    List<Forces> list(ForcesCondition condition);

    /**
     * 根据试验id查询兵力，包括兵力的搭载
     *
     * @param experimentId
     * @return
     */
    List<Forces> seekByExperiment(String experimentId);

    /**
     * 根据试验id和阵营查询兵力
     *
     * @param experimentId
     * @param team
     * @return 兵力的简要信息
     */
    List<String> queryByExperiment(String experimentId, String team);

    /**
     * modelde input 新增后 更新兵力的input,
     * @param forcesId
     */
    public void flashInput(String forcesId);
}
