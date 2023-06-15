package com.juntai.tinder.cache;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.juntai.tinder.config.ApplicationContextProvider;
import com.juntai.tinder.entity.TaskType;
import com.juntai.tinder.facade.TaskTypeFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Optional;


/**
 * @Description:
 * @Author: nemo
 * @Date: 2023/2/9
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class TaskTypeCache {
    protected Cache<String, TaskType> cache = Caffeine.newBuilder()
            .maximumSize(10_000)
            .expireAfterWrite(Duration.ofMinutes(10))
            .build();

    public TaskType getCacheData(String id) {
        TaskTypeFacade taskTypeFacade = ApplicationContextProvider.getBean(TaskTypeFacade.class);
        return cache.get(id, key -> Optional.ofNullable(taskTypeFacade.getById(key)).orElse(new TaskType()));
    }

    public void setCacheData(String id, TaskType taskType) {
        cache.put(id, taskType);
    }

    public void clearCacheData() {
        cache.invalidateAll();
    }

    public void clearCacheData(String id) {
        cache.invalidate(id);
    }

    public void refreshCache() {
        TaskTypeFacade taskTypeFacade = ApplicationContextProvider.getBean(TaskTypeFacade.class);
        List<TaskType> all = taskTypeFacade.getAll();
        for (TaskType taskType : all) {
            setCacheData(taskType.getId(), taskType);
        }
    }
}
