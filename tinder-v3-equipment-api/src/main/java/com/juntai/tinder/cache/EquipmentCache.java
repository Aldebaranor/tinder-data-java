package com.juntai.tinder.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.juntai.tinder.config.ApplicationContextProvider;
import com.juntai.tinder.entity.Equipment;
import com.juntai.tinder.facade.EquipmentFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

/**
 * @author ：haungkang
 * @description：装备信息缓存
 * @version: 1.0.0
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class EquipmentCache {
    protected Cache<String, Equipment> cache = Caffeine.newBuilder()
            .maximumSize(10_000)
            .expireAfterWrite(Duration.ofMinutes(10))
            .build();

    public Equipment getCacheData(String id) {
        EquipmentFacade equipmentFacade = ApplicationContextProvider.getBean(EquipmentFacade.class);
        return cache.get(id, key -> Optional.ofNullable(equipmentFacade.seekById(key)).orElse(new Equipment()));
    }

    public void setCacheData(String id, Equipment equipment) {
        cache.put(id, equipment);
    }

    public void clearCacheData() {
        cache.invalidateAll();
    }

    public void clearCacheData(String defineId) {
        cache.invalidate(defineId);
    }

    public void clearCacheDataList(List<String> defineIds) {
        defineIds.forEach(q -> {
            cache.invalidate(q);
        });

    }

    public void refreshCache() {
        EquipmentFacade equipmentFacade = ApplicationContextProvider.getBean(EquipmentFacade.class);
        List<Equipment> all = equipmentFacade.getAll();
        for (Equipment equipment : all) {
            Equipment seek = equipmentFacade.seek(equipment);
            setCacheData(seek.getId(), seek);
        }
    }
}
