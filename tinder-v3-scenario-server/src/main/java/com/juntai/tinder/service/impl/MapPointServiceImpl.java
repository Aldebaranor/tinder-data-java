package com.juntai.tinder.service.impl;

import com.juntai.tinder.entity.MapPoint;
import com.juntai.tinder.mapper.MapPointMapper;
import com.juntai.tinder.service.MapPointService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.List;
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
public class MapPointServiceImpl  implements MapPointService {

    @Override
    public MapPoint getById(String id) {
        return null;
    }

    @Override
    public String insert(MapPoint entity) {
        mapPoint.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return super.insert(mapPoint);
    }

    @Override
    public void update(MapPoint entity) {
        mapPoint.setModifyTime(new Timestamp(System.currentTimeMillis()));
        super.update(mapPoint);
    }

    @Override
    public void save(MapPoint entity) {
        if (StringUtils.isBlank(entity.getId())) {
            entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
            super.insert(entity);
        } else {
            entity.setModifyTime(new Timestamp(System.currentTimeMillis()));
            super.update(entity);
        }
    }

    @Override
    public int deleteById(String id) {
        return 0;
    }

    @Override
    public List<MapPoint> getByPageCode(String pageCode) {
        Clause clause;
        clause = CombineClause.and(
                SingleClause.equal("pageCode", pageCode),
                SingleClause.equal("disabled", 0)
        );
        return mapPointRepository.query(clause).stream().collect(Collectors.toList());
    }

    @Override
    public Boolean setDisabledById(String id) {
        MapPoint mapPoint = super.getById(id);
        if (mapPoint == null) {
            throw ExceptionUtils.api("没有相应的数据");
        }
        mapPoint.setModifyTime(new Timestamp(System.currentTimeMillis()));
        mapPoint.setDisabled(true);
        super.update(mapPoint);
        return true;
    }

    @Override
    public MapPoint getByExperimentId(String experimentId) {
        MapPointCondition mapPointCondition = new MapPointCondition();
        mapPointCondition.setExperimentId(experimentId);
        return super.first(mapPointCondition);
    }

    @Override
    public void deleteByExperimentId(String experimentId) {
        MapPointCondition mapPointCondition = new MapPointCondition();
        mapPointCondition.setExperimentId(experimentId);
        List<MapPoint> query = super.query(mapPointCondition);
        if(CollectionUtils.isEmpty(query)){
            return;
        }
        super.deleteByIds(query.stream().map(MapPoint::getId).collect(Collectors.toList()));
    }
}
