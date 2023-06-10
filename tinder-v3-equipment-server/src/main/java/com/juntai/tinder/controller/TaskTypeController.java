package com.juntai.tinder.controller;

import com.juntai.soulboot.common.exception.SoulBootException;
import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.soulboot.web.api.ApiResultWrap;
import com.juntai.tinder.cache.TaskTypeCache;
import com.juntai.tinder.condition.TaskTypeCondition;
import com.juntai.tinder.entity.TaskType;
import com.juntai.tinder.exception.TinderErrorCode;
import com.juntai.tinder.service.TaskTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author nemo
 * @since 2023-06-07
 */
@ApiResultWrap
@RestController
@RequestMapping("/tinder/v3/task-type")
public class TaskTypeController {
    @Autowired
    private TaskTypeService taskTypeService;

    @Autowired
    private  TaskTypeCache taskTypeCache;

    @GetMapping(value = "/{id}")
    public TaskType getById(@PathVariable("id") String id) {
        return taskTypeService.getById(id);
    }

    @GetMapping(value = "/all")
    public List<TaskType> list() {
        List<TaskType> all = taskTypeService.getAll();
        for (TaskType taskType : all) {
            taskType.setAttributes("");
        }
        return all;
    }

    /**
     * 保存
     *
     * @param entity
     * @return 主键
     */
    @PostMapping
    public String insert(@RequestBody TaskType entity) {
        if (entity.getType() == null) {
            throw new SoulBootException(TinderErrorCode.TINDER_TASK_TYPE_ERROR,"任务类型不能为空");
        }
        if (StringUtils.isBlank(entity.getName())) {
            throw new SoulBootException(TinderErrorCode.TINDER_TASK_TYPE_ERROR,"任务名称不能为空");
        }
        return taskTypeService.insert(entity);
    }

    /**
     * 更新
     *
     * @param entity
     */
    @PutMapping
    public void update(@RequestBody TaskType entity) {
        taskTypeService.update(entity);
    }

    /**
     * 主键删除
     *
     * @param id 主键
     * @return 影响记录行数
     */
    @DeleteMapping(value = "/{id}")
    public int deleteById(@PathVariable String id) {
        return taskTypeService.deleteById(id);
    }

    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    @PostMapping("/page")
    public Pagination<TaskType> page(@RequestBody Query<TaskTypeCondition,TaskType> query) {
        return taskTypeService.page(query);
    }

    @DeleteMapping("/batch")
    public int batchDelete(@RequestBody List<String> ids) {
        return taskTypeService.deleteByIds(ids);
    }
}
