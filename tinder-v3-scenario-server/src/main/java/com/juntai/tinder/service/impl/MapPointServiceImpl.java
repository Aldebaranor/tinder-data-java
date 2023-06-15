package com.juntai.tinder.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.juntai.soulboot.common.exception.SoulBootException;
import com.juntai.tinder.entity.MapPoint;
import com.juntai.tinder.exception.TinderErrorCode;
import com.juntai.tinder.mapper.MapPointMapper;
import com.juntai.tinder.service.MapPointService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * <p>
 * 地图预置名称 服务实现类
 * </p>
 *
 * @author nemo
 * @since 2023-06-07
 */
@Service
public class MapPointServiceImpl implements MapPointService {

    @Autowired
    private MapPointMapper mapper;

    @Override
    public MapPoint getById(String id) {
        return mapper.selectById(id);
    }

    @Override
    public String insert(MapPoint entity) {
        String id = UUID.randomUUID().toString();
        entity.setId(id);
        entity.setCreateTime(LocalDateTime.now());
        mapper.insert(entity);
        return id;
    }

    @Override
    public void update(MapPoint entity) {
        entity.setModifyTime(LocalDateTime.now());
        mapper.updateById(entity);
    }

    @Override
    public void save(MapPoint entity) {
        if (StringUtils.isBlank(entity.getId())) {
            insert(entity);
        } else {
            update(entity);
        }
    }

    @Override
    public int deleteById(String id) {
        return mapper.deleteById(id);
    }

    @Override
    public MapPoint getByExperimentId(String experimentId) {
        return new LambdaQueryChainWrapper<>(mapper).eq(MapPoint::getDisabled, 0)
                .eq(MapPoint::getExperimentId, experimentId).last("limit 1").one();
    }

    @Override
    public int setDisabledById(String id) {
        MapPoint mapPoint = this.getById(id);
        if (mapPoint == null) {
            throw new SoulBootException(TinderErrorCode.TINDER_MAP_POINT_ERROR, "没有相应的数据");
        }
        mapPoint.setModifyTime(LocalDateTime.now());
        mapPoint.setDisabled(true);
        return mapper.updateById(mapPoint);
    }


    @Override
    public void deleteByExperimentId(String experimentId) {
        List<MapPoint> query = new LambdaQueryChainWrapper<>(mapper)
                .eq(MapPoint::getExperimentId, experimentId).list();
        if (CollectionUtils.isEmpty(query)) {
            return;
        }
        mapper.deleteBatchIds(query.stream().map(MapPoint::getId).collect(Collectors.toList()));
    }
}
