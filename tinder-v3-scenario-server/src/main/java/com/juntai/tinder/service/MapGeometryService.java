package com.juntai.tinder.service;

import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.tinder.condition.MapGeometryCondition;
import com.juntai.tinder.entity.MapGeometry;
import com.juntai.tinder.facade.MapGeometryFacade;
import com.juntai.tinder.model.GeometryModel;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author nemo
 * @since 2023-06-07
 */
public interface MapGeometryService extends MapGeometryFacade {
    /**
     * 根据阵营和试验号查询
     *
     * @param experimentId
     * @param team
     * @return
     */
    List<MapGeometry> getByExperiment(String experimentId, String team);

    /**
     * 根据试验id和阵营查询
     *
     * @param experimentId
     * @param team
     * @return
     */
    List<GeometryModel> getGeometryModelByExperiment(String experimentId, String team);

    /**
     * 条件查询
     *
     * @param condition
     * @return
     */
    List<MapGeometry> query(MapGeometryCondition condition);

    /**
     * 分页查询
     *
     * @param mode
     * @return
     */
    Pagination<MapGeometry> page(Query<MapGeometryCondition, MapGeometry> mode);

    /**
     * 主键查询
     *
     * @param id
     * @return
     */
    MapGeometry getById(String id);

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    String insert(MapGeometry entity);

    /**
     * @param experimentId
     * @param team
     * @param json
     */
    void insertJson(String experimentId, String team, String json);

    /**
     * 保存
     *
     * @param experimentId
     * @param team
     * @param json
     * @return
     */
    void updateListByJson(String experimentId, String team, List<String> json);

    /**
     * 更新
     *
     * @param experimentId
     * @param team
     * @param json
     */
    void saveOneByJson(String experimentId, String team, String json);


    /**
     * 删除
     *
     * @param id
     * @return
     */
    int deleteById(String id);

    void update(MapGeometry entity);
}
