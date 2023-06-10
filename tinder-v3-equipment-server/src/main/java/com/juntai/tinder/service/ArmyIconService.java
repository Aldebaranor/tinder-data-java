package com.juntai.tinder.service;

import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.tinder.condition.ArmyIconCondition;
import com.juntai.tinder.entity.ArmyIcon;
import com.juntai.tinder.facade.ArmyIconFacade;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author nemo
 * @since 2023-05-07
 */
public interface ArmyIconService extends ArmyIconFacade {


    /**
     * 列表查询
     * @param condition
     * @return
     */
    List<ArmyIcon> list(ArmyIconCondition condition);

    /**
     * 分页查询
     * @param query
     * @return
     */
    Pagination<ArmyIcon> page(Query<ArmyIconCondition, ArmyIcon> query);


    /**
     * 更新
     * @param entity
     * @return
     */
    int update(ArmyIcon entity) ;

    /**
     * 新增
     * @param entity
     * @return
     */
    String insert(ArmyIcon entity) ;

    /**
     * 主键删除
     *
     * @param id 主键
     * @return 成功与否
     */

    int deleteById(String id);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    int deleteByIds(List<String> ids);



}
