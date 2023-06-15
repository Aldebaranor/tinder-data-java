package com.juntai.tinder.service;

import com.juntai.tinder.condition.ForcesLibraryCondition;
import com.juntai.tinder.entity.ForcesLibrary;
import com.juntai.tinder.facade.ForcesLibraryFacade;
import com.juntai.tinder.model.ForcesLibraryUpdateModel;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author nemo
 * @since 2023-06-07
 */
public interface ForcesLibraryService extends ForcesLibraryFacade {

    /**
     * 主键查询
     *
     * @param id 主键
     * @return ExperimentShare
     */

    ForcesLibrary getById(String id);

    /**
     * 获取更新过名称的ForcesLibrary
     *
     * @param id
     * @param experimentId
     * @return
     */
    ForcesLibrary getNewId(String id, String experimentId);

    /**
     * 关联查询
     *
     * @param id
     * @return
     */
    ForcesLibrary seekById(String id);

    /**
     * 保存
     *
     * @param entity 兵力
     * @return 主键
     */

    String insert(ForcesLibrary entity);

    /**
     * 批量保存
     *
     * @param entity
     */
    void insertList(List<ForcesLibrary> entity);

    /**
     * 用于复制想定中的兵力复制
     *
     * @param id
     * @param experimentId
     * @return
     */
    String copyForces(String id, String experimentId);


    /**
     * 添加兵力
     *
     * @param experimentId
     * @param team
     * @param equipmentId
     * @param modelId
     * @return
     */
    String addForces(String experimentId, String team, String equipmentId, String modelId);

    /**
     * 添加兵力搭载
     *
     * @param experimentId
     * @param team
     * @param equipmentId
     * @param modelId
     * @param belongId
     * @param num
     * @return
     */
    String addRelation(String experimentId, String team, String equipmentId, String modelId, String belongId, int num);

    /**
     * 主键删除
     *
     * @param id 主键
     * @return 影响记录行数
     */

    int deleteById(String id);

    /**
     * 查询所有兵力，不处理兵力搭载
     *
     * @param condition
     * @return List<Forces>
     */
    List<ForcesLibrary> list(ForcesLibraryCondition condition);

    /**
     * 根据试验id和阵营查询兵力以及兵力的搭载
     *
     * @param experimentId
     * @param team
     * @return
     */
    List<ForcesLibrary> queryByExperiment(String experimentId, String team);

    /**
     * 更新兵力
     *
     * @param entity
     */
    void update(ForcesLibraryUpdateModel entity);


    /**
     * 更新搭载
     *
     * @param belongId
     * @param entity
     */
    void updateCarry(String belongId, ForcesLibraryUpdateModel entity);

    /**
     * 批量更新
     *
     * @param belongId
     * @param list
     */
    void updateCarryList(String belongId, List<ForcesLibraryUpdateModel> list);

    /**
     * 根据id更新搭载
     *
     * @param belongId
     * @param id
     * @return
     */
    int deleteCarryById(String belongId, String id);

    /**
     * 修改兵力资源库的名字
     *
     * @param id
     * @param newName
     */
    void updateName(String id, String newName);

    /**
     * modelde input 新增后 更新兵力的input,
     *
     * @param forcesId
     */
    public void flashInput(String forcesId);
}
