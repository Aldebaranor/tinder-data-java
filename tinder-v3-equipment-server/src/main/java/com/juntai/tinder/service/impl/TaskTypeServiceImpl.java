package com.juntai.tinder.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.juntai.soulboot.data.ConditionParser;
import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.tinder.cache.TaskTypeCache;
import com.juntai.tinder.condition.TaskTypeCondition;
import com.juntai.tinder.entity.Equipment;
import com.juntai.tinder.entity.TaskType;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juntai.tinder.mapper.TaskTypeMapper;
import com.juntai.tinder.service.TaskTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author nemo
 * @since 2023-06-07
 */
@Service
public class TaskTypeServiceImpl  implements TaskTypeService {

    @Autowired
    TaskTypeMapper mapper;

    @Autowired
    TaskTypeCache taskTypeCache;

    @Override
    @Transactional(readOnly = true)
    public TaskType getById(String id) {
        return mapper.selectById(id);
    }

    @Override
    public List<TaskType> getAll() {
        return new LambdaQueryChainWrapper<>(mapper).list();
    }

    @Override
    public TaskType getByCode(String code) {
        return new LambdaQueryChainWrapper<>(mapper).eq(TaskType::getCode,code).one();
    }

    @Override
    public List<TaskType> getByType(String type) {
        return new LambdaQueryChainWrapper<>(mapper).eq(TaskType::getType,type).list();
    }

    @Override
    public List<TaskType> list(TaskTypeCondition condition) {

        QueryChainWrapper<TaskType> wrapper = ChainWrappers.queryChain(TaskType.class);;
        ConditionParser.parse(wrapper, condition);
        return wrapper.list();
    }

    @Override
    public String insert(TaskType entity) {
        String id = getNewId(entity.getType().getValue());
        entity.setId(id);
        entity.setCode(id);
        mapper.insert(entity);
        taskTypeCache.setCacheData(entity.getId(), entity);
        return id;
    }

    @Override
    public void update(TaskType entity) {
        mapper.updateById(entity);
        taskTypeCache.setCacheData(entity.getId(),entity);
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
    public Pagination<TaskType> page(Query<TaskTypeCondition, TaskType> query) {
        QueryChainWrapper<TaskType> wrapper = ChainWrappers.queryChain(TaskType.class);;
        ConditionParser.parse(wrapper, query.getCondition());
        return wrapper.page(query.toPage());

    }

    private String getNewId(String type) {
        List<TaskType> query = new LambdaQueryChainWrapper<>(mapper).eq(TaskType::getType,type).list();
        Long max = 0L;
        for (TaskType taskType : query) {
            String replace = taskType.getId().replaceFirst(type, "");
            Long aLong = Long.valueOf(replace);
            if (aLong > max) {
                max = aLong;
            }
        }
        return String.format("%s%s", type, StringUtils.leftPad(String.valueOf(max + 1), 2, "0"));


    }
}
