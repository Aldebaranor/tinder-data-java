package com.juntai.tinder.facade;

import com.juntai.tinder.entity.ModelRelation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/5/30
 */
public interface ModelRelationFacade {


    /**
     * 主键查询
     *
     * @param id 主键
     * @return OperateResult
     */
    ModelRelation seekById( String id);


    /**
     * modelId查询
     *
     * @param modelId
     * @return OperateResult
     */

    List<ModelRelation> modelId(String modelId);

}
