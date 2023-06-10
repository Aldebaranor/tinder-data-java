package com.juntai.tinder.facade;

import com.juntai.tinder.condition.ModelCondition;
import com.juntai.tinder.entity.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/5/30
 */
public interface ModelFacade {


    /**
     * 主键查询
     *
     * @param id 主键
     * @return OperateResult
     */

    Model seekById(String id);


    /**
     * 获取全部
     *
     * @param
     * @return java.util.List<com.soul.meta.entity.Model>
     * @author huangkang
     * @desc
     * @date 2022/4/30 23:46
     */

    List<Model> getAll();

    /**
     * 计算总数
     *
     * @param condition
     * @return
     */

    Long count(ModelCondition condition);

    /**
     * 保存
     *
     * @param entity
     * @return OperateResult
     */

    String insert(Model entity);

    /**
     * 更新
     *
     * @param entity
     * @return OperateResult
     */

    void update(Model entity);

    /**
     * 主键删除
     *
     * @param id 主键
     * @return OperateResult
     */

    int deleteById(String id);

    List<Model> list(ModelCondition condition);

    Model getById(String id);


//    /**
//     * 获取关联的网盘文件
//     *
//     * @param id
//     * @return java.util.List<com.egova.pan.entity.FileInfo>
//     * @author huangkang
//     * @desc
//     * @date 2022/5/1 0:08
//     */
//    @GetMapping(value = "/pan-files/{id}")
//    List<FileInfo> getFiles(@PathVariable("id") String id);
}
