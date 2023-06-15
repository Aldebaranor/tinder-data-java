package com.juntai.tinder.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.google.common.base.CaseFormat;
import com.juntai.soulboot.data.ConditionParser;
import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.soulboot.util.JsonUtils;
import com.juntai.tinder.cache.TaskTypeCache;
import com.juntai.tinder.condition.EquipmentTaskCondition;
import com.juntai.tinder.entity.EquipmentTask;
import com.juntai.tinder.entity.TaskType;
import com.juntai.tinder.mapper.EquipmentTaskMapper;
import com.juntai.tinder.service.EquipmentTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * <p>
 * 装备名称 服务实现类
 * </p>
 *
 * @author nemo
 * @since 2023-05-07
 */
@Service
public class EquipmentTaskServiceImpl implements EquipmentTaskService {
    @Autowired
    private EquipmentTaskMapper mapper;

    @Autowired
    private TaskTypeCache taskTypeCache;

    @Override
    @Transactional(readOnly = true)
    public EquipmentTask getById(String id) {
        return buildEquipmentTask(mapper.selectById(id));
    }


    @Override
    public List<EquipmentTask> getAll() {
        return new LambdaQueryChainWrapper<>(mapper).list();
    }

    @Override
    public List<String> containTask(String task) {
        List<EquipmentTask> list = new LambdaQueryChainWrapper<>(mapper).like(EquipmentTask::getTasks, task).list();
        return list.stream().map(EquipmentTask::getEquipmentId).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipmentTask> list(EquipmentTaskCondition condition) {
        QueryChainWrapper<EquipmentTask> wrapper = ChainWrappers.queryChain(EquipmentTask.class);
        ;
        ConditionParser.parse(wrapper, condition);
        List<EquipmentTask> list = wrapper.list();
        list.forEach(q -> {
            List<String> ids = JsonUtils.readList(q.getTasks(), String.class);
            List<String> names = new ArrayList<>();
            for (String s : ids) {
                TaskType cacheData = taskTypeCache.getCacheData(s);
                if (cacheData != null) {
                    names.add(String.format("%s(%s)", cacheData.getName(), cacheData.getCode()));
                }
            }

            q.setTaskNames(JsonUtils.write(names));
        });
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public Pagination<EquipmentTask> page(Query<EquipmentTaskCondition, EquipmentTask> query) {
        QueryChainWrapper<EquipmentTask> wrapper = ChainWrappers.queryChain(EquipmentTask.class);
        ;
        ConditionParser.parse(wrapper, query.getCondition());
        Pagination<EquipmentTask> page = wrapper.page(query.toPage());
        page.getList().forEach(q -> {
            List<String> ids = JsonUtils.readList(q.getTasks(), String.class);
            List<String> names = new ArrayList<>();
            for (String s : ids) {
                TaskType cacheData = taskTypeCache.getCacheData(s);
                if (cacheData != null) {
                    names.add(String.format("%s(%s)", cacheData.getName(), cacheData.getCode()));
                }
            }

            q.setTaskNames(JsonUtils.write(names));
        });

        return page;
    }

    @Override
    public int update(EquipmentTask entity) {
        return mapper.updateById(entity);
    }

    @Override
    public void insertList(List<EquipmentTask> list) {
        list.forEach(q -> {
            String id = UUID.randomUUID().toString();
            q.setId(id);
            mapper.insert(q);
        });

    }

    @Override
    public String insert(EquipmentTask entity) {
        String id = UUID.randomUUID().toString();
        entity.setId(id);
        mapper.insert(entity);
        return id;
    }


    @Override
    public int deleteById(String id) {
        return mapper.deleteById(id);
    }

    @Override
    public int deleteByIds(List<String> ids) {
        return mapper.deleteBatchIds(ids);
    }

    @Override
    public EquipmentTask getByEquipmentId(String equipmentId) {
        EquipmentTask equipmentTask = new LambdaQueryChainWrapper<>(mapper).eq(EquipmentTask::getEquipmentId, equipmentId).one();
        return buildEquipmentTask(equipmentTask);
    }

    public EquipmentTask buildEquipmentTask(EquipmentTask equipmentTask) {
        if (equipmentTask == null) {
            return null;
        }
        List<String> ids = JsonUtils.readList(equipmentTask.getTasks(), String.class);
        List<TaskType> list = new ArrayList<>();
        List<String> names = new ArrayList<>();
        for (String s : ids) {
            TaskType cacheData = taskTypeCache.getCacheData(s);
            if (cacheData != null) {
                list.add(cacheData);
                names.add(String.format("%s(%s)", cacheData.getName(), cacheData.getCode()));
            }
        }
        equipmentTask.setTaskList(list);
        equipmentTask.setTaskNames(JsonUtils.write(names));
        return equipmentTask;
    }

    @Override
    public void modify(String id, HashMap<String, Object> map) {


        UpdateWrapper<EquipmentTask> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", id);
        map.forEach((k, v) -> {
            k = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, k);
            wrapper.set(k, v);
        });
        mapper.update(null, wrapper);

    }
}
