package com.juntai.tinder.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.juntai.tinder.config.ApplicationContextProvider;
import com.juntai.tinder.entity.Model;
import com.juntai.tinder.facade.ModelFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

/**
 * @author ：haungkang
 * @description：模型缓存
 * @version: 1.0.0
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ModelCache {

    protected Cache<String, Model> cache = Caffeine.newBuilder()
            .maximumSize(10_000)
            .expireAfterWrite(Duration.ofMinutes(10))
            .build();

    public Model getCacheData(String id) {
        ModelFacade modelFacade = ApplicationContextProvider.getBean(ModelFacade.class);
        return cache.get(id, key -> Optional.ofNullable(modelFacade.seekById(key)).orElse(null));
    }

    public void setCacheData(String id, Model model) {
        cache.put(id, model);
    }

    public void clearCacheData() {
        cache.invalidateAll();
    }

    public void clearCacheData(String id) {
        cache.invalidate(id);
    }

    public void clearCacheDataList(List<String> ids) {
        ids.forEach(q -> {
            cache.invalidate(q);
        });

    }

    public void refreshCache() {
        ModelFacade modelFacade = ApplicationContextProvider.getBean(ModelFacade.class);
        List<Model> all = modelFacade.getAll();
        for (Model model : all) {

            setCacheData(model.getId(), model);
        }
    }
}
