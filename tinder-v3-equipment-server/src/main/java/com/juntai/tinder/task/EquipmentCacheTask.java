package com.juntai.tinder.task;

import com.juntai.tinder.cache.EquipmentCache;
import com.juntai.tinder.cache.ModelCache;
import com.juntai.tinder.cache.TaskTypeCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author ：huangkang
 * @date ： 2022/4/30 23:39
 * @description：装备模型缓存任务
 * @version: 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EquipmentCacheTask {

    private final EquipmentCache equipmentCache;
    private final ModelCache modelCache;
    private final TaskTypeCache taskTypeCache;


    @Scheduled(fixedRateString = "PT10M")
    public void refreshCache() {
        log.info("刷新数据源表信息开始");
        try {
            equipmentCache.refreshCache();
            modelCache.refreshCache();
            taskTypeCache.refreshCache();
        } catch (Exception e) {
            log.error("刷新数据源表信息出错", e);
        }
        log.info("刷新数据源表信息结束");
    }
}
