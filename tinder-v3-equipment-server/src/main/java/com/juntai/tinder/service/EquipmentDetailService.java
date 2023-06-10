package com.juntai.tinder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.tinder.entity.EquipmentDetail;
import com.juntai.tinder.facade.EquipmentDetailFacade;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * <p>
 * 装备模型库用来显示的 服务类
 * </p>
 *
 * @author nemo
 * @since 2023-05-07
 */
public interface EquipmentDetailService extends EquipmentDetailFacade {
    /**
     * 主键查询
     *
     * @param id
     * @return
     */
    EquipmentDetail getById(String id);


    /**
     * 更新
     *
     * @param entity
     * @return
     */
    int update(EquipmentDetail entity);

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    String insert(EquipmentDetail entity);

    /**
     * 主键删除
     *
     * @param id 主键
     * @return 成功与否
     */

    int deleteById(String id);

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    int deleteByIds(List<String> ids);


    /**
     * 更新基本信息
     *
     * @param attributeInfo
     */
    void updateAttributeInfo(String id, String attributeInfo);

}
