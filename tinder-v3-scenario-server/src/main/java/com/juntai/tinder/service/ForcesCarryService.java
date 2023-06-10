package com.juntai.tinder.service;

import com.juntai.tinder.condition.ForcesCarryCondition;
import com.juntai.tinder.entity.ForcesCarry;
import com.baomidou.mybatisplus.extension.service.IService;
import com.juntai.tinder.facade.ForcesCarryFacade;
import com.juntai.tinder.model.ForcesLibraryUpdateModel;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author nemo
 * @since 2023-06-07
 */
public interface ForcesCarryService extends ForcesCarryFacade {
    /**
     * 主键查询
     *
     * @param id 主键
     * @return ExperimentShare
     */

    ForcesCarry getById(String id);

    /**
     * 保存
     *
     * @param entity 兵力搭载
     * @return 主键
     */

    String insert(ForcesCarry entity);

    /**
     * 批量插入
     *
     * @param entity
     */
    void insertList(List<ForcesCarry> entity);

    /**
     * 新增兵力搭载
     *
     * @param experimentId 试验ID
     * @param parentId     父兵力
     * @param equipmentId    搭载兵力的装备信息
     * @param modelId      模型ID
     * @param num          搭载数量
     * @return
     */
    String addForcesCarry(String experimentId, String parentId, String equipmentId, String modelId, int num);

    /**
     * 更新
     *
     * @param belongId
     * @param entity
     */
    void update(String belongId, ForcesLibraryUpdateModel entity);

    /**
     * 批量修改
     *
     * @param belongId
     * @param list
     */
    void modifyList(String belongId, List<ForcesLibraryUpdateModel> list);


    /**
     * 保存
     *
     * @param list
     */
    void save(List<ForcesCarry> list);

    /**
     * 主键删除
     *
     * @param id 主键
     * @return 影响记录行数
     */

    int deleteById(String id);

    /**
     * 判断是否存在
     *
     * @param condition
     * @return
     */
    boolean exists(ForcesCarryCondition condition);

    /**
     * 主键批量删除
     *
     * @param ids 主键
     * @return 影响记录行数
     */
    int deleteByIds(List<String> ids);

    /**
     * 查询所有兵力
     *
     * @param condition
     * @return List<Forces>
     */
    List<ForcesCarry> list(ForcesCarryCondition condition);

    /**
     * 根据试验Id和兵力ID查询兵力搭载
     *
     * @param forcesId
     * @param experimentId
     * @return
     */
    List<ForcesCarry> getByForcesId(String forcesId, String experimentId);
}
