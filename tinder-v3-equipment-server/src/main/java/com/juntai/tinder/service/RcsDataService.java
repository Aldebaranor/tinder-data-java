package com.juntai.tinder.service;

import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.tinder.condition.RcsDataCondition;
import com.juntai.tinder.entity.RcsData;
import com.juntai.tinder.facade.RcsDataFacade;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author nemo
 * @since 2023-06-07
 */
public interface RcsDataService extends RcsDataFacade {

    /**
     * 分页查询
     *
     * @param query QueryModel
     * @return 分页数据
     */
    Pagination<RcsData> page(Query<RcsDataCondition, RcsData> query);

    /**
     * 主键批量删除
     *
     * @param ids 主键
     * @return 影响记录行数
     */
    int deleteByIds(List<String> ids);

    /**
     * 列表查询
     *
     * @param condition SourceCondition
     * @return 列表数据
     */
    List<RcsData> list(RcsDataCondition condition);
}
