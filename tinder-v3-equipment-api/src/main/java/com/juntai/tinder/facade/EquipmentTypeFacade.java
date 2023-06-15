package com.juntai.tinder.facade;

import com.juntai.tinder.entity.EquipmentType;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;


/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/4/1
 */
public interface EquipmentTypeFacade {

    /**
     * 主键查询
     *
     * @param id 主键
     * @return EquipmentType
     */

    EquipmentType getById(String id);

    /**
     * 获取子类
     *
     * @param id
     * @return
     */
    List<EquipmentType> children(String id);

    /**
     * 获取所有的子类
     *
     * @param id
     * @return
     */
    List<EquipmentType> childrens(String id);

//    /**
//     * id-name map
//     *
//     * @return map
//     */
//    Map<String, String> getNameMapById();

    /**
     * 根据id 找名称
     *
     * @param id
     * @return
     */
    @Cacheable(cacheNames = "tinder:cache:equipment-type:name", key = "'id:'+#p0")
    String getNameById(String id);

}
