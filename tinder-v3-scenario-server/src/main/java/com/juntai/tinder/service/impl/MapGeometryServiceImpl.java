package com.juntai.tinder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juntai.soulboot.common.exception.SoulBootException;
import com.juntai.soulboot.data.ConditionParser;
import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.soulboot.util.JsonUtils;
import com.juntai.tinder.condition.MapGeometryCondition;
import com.juntai.tinder.entity.MapGeometry;
import com.juntai.tinder.entity.enums.GeometryFunctionType;
import com.juntai.tinder.entity.enums.GeometryType;
import com.juntai.tinder.entity.enums.TeamType;
import com.juntai.tinder.exception.TinderErrorCode;
import com.juntai.tinder.mapper.MapGeometryMapper;
import com.juntai.tinder.model.GeometryModel;
import com.juntai.tinder.model.Properties;
import com.juntai.tinder.service.MapGeometryService;
import com.juntai.tinder.utils.GeometryUtils;
import com.juntai.tinder.utils.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author nemo
 * @since 2023-06-07
 */
@Service
public class MapGeometryServiceImpl implements MapGeometryService {

    @Autowired
    private MapGeometryMapper mapper;

    @Override
    public List<MapGeometry> getByExperiment(String experimentId, String team) {
        //Clause clause;
        LambdaQueryWrapper<MapGeometry> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MapGeometry::getExperimentId, experimentId);

        //白方可以查询到所有，红方查询到白方与红方，蓝方查询到白方与蓝方
        if (StringUtils.equals(TeamType.RED.getValue(), team)) {
            wrapper.in(MapGeometry::getTeam, new Object[]{TeamType.RED.getValue(), TeamType.WHITE.getValue()});
        } else if (StringUtils.equals(TeamType.BLUE.getValue(), team)) {
            wrapper.in(MapGeometry::getTeam, new Object[]{TeamType.BLUE.getValue(), TeamType.WHITE.getValue()});
        }
        return mapper.selectList(wrapper);
    }

    @Override
    public List<GeometryModel> getGeometryModelByExperiment(String experimentId, String team) {
        List<MapGeometry> byExperiment = getByExperiment(experimentId, team);
        return convertGeometryModel(byExperiment);
    }

    @Override
    public List<MapGeometry> query(MapGeometryCondition condition) {
        QueryChainWrapper<MapGeometry> wrapper = ChainWrappers.queryChain(MapGeometry.class);
        ;
        ConditionParser.parse(wrapper, condition);
        return wrapper.list();
    }

    @Override
    public Pagination<MapGeometry> page(Query<MapGeometryCondition, MapGeometry> query) {
        QueryChainWrapper<MapGeometry> wrapper = ChainWrappers.queryChain(MapGeometry.class);
        ;
        ConditionParser.parse(wrapper, query.getCondition());
        return wrapper.page(query.toPage(MapGeometry.class));
    }

    @Override
    public MapGeometry getById(String id) {
        return mapper.selectById(id);
    }

    @Override
    public String insert(MapGeometry entity) {
        String id = UUIDUtils.getHashUuid();
        entity.setId(id);
        entity.setCreateTime(LocalDateTime.now());
        mapper.insert(entity);
        return id;
    }

    @Override
    public void insertJson(String experimentId, String team, String json) {
        List<MapGeometry> list = new ArrayList<>();
        List<Object> jsonList = JsonUtils.readList(json, Object.class);
        jsonList.forEach(s -> {
            try {
                MapGeometry entity = new MapGeometry();
                entity.setExperimentId(experimentId);
                entity.setTeam(team);
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(JsonUtils.write(s));
                entity.setId(node.findValue("id").asText());
                JsonNode areaType = node.findValue("areaType");
                if (areaType != null && !StringUtils.isBlank(areaType.asText())) {
                    for (GeometryFunctionType typeEnum : GeometryFunctionType.values()) {
                        if (typeEnum.getValue().equals(areaType)) {
                            entity.setType(typeEnum);
                            break;
                        }
                    }
                }
                entity.setName(node.findValue("name").asText());
                JsonNode geometryNode = node.findValue("geometry");
                String type = geometryNode.findValue("type").asText();
                if (StringUtils.isBlank(type)) {
                    throw new SoulBootException(TinderErrorCode.TINDER_GEOMETRY_ERROR, "geometry.type 不能为空");
                }
                entity.setGeometryType(type);
                entity.setGeometry(JsonUtils.write(s));

                JsonNode properties = node.findValue("properties");
                String propertiesType = properties.findValue("type").asText();
                if (StringUtils.isBlank(propertiesType)) {
                    throw new SoulBootException(TinderErrorCode.TINDER_GEOMETRY_ERROR, "properties.type 不能为空");
                }
                entity.setPropertiesType(propertiesType);
                entity.setCreateTime(LocalDateTime.now());
                list.add(entity);
            } catch (JsonProcessingException e) {
                throw new SoulBootException(TinderErrorCode.TINDER_GEOMETRY_ERROR, e.getMessage());
            }

        });
        list.forEach(q -> {
            mapper.insert(q);
        });
    }

    @Override
    public void updateListByJson(String experimentId, String team, List<String> jsonList) {
        MapGeometryCondition condition = new MapGeometryCondition();
        List<MapGeometry> list = new LambdaQueryChainWrapper<>(mapper).eq(MapGeometry::getTeam, team)
                .eq(MapGeometry::getExperimentId, experimentId).list();
        for (String s : jsonList) {
            try {
                Object object = JsonUtils.read(s, Object.class);
                ObjectMapper map = new ObjectMapper();
                JsonNode node = map.readTree(JsonUtils.write(object));
                String id = node.findValue("id").asText();
                MapGeometry mapGeometry = list.stream().filter(q -> StringUtils.equals(q.getId(), id)).findFirst().orElse(null);
                if (mapGeometry == null) {
                    continue;
                }
                mapGeometry.setGeometry(JsonUtils.write(object));
                mapGeometry.setModifyTime(LocalDateTime.now());
                mapper.updateById(mapGeometry);
            } catch (JsonProcessingException e) {
                throw new SoulBootException(TinderErrorCode.TINDER_GEOMETRY_ERROR, e.getMessage());
            }

        }

    }

    @Override
    public void saveOneByJson(String experimentId, String team, String json) {

        try {
            MapGeometry entity = convert(experimentId, team, json);
            if (mapper.selectById(entity.getId()) == null) {
                mapper.insert(entity);
            } else {
                mapper.updateById(entity);
            }
        } catch (Exception e) {
            throw new SoulBootException(TinderErrorCode.TINDER_GEOMETRY_ERROR, e.getMessage());
        }
    }

    @Override
    public int deleteById(String id) {
        return mapper.deleteById(id);
    }

    @Override
    public void update(MapGeometry entity) {
        entity.setModifyTime(LocalDateTime.now());
        mapper.updateById(entity);
    }

    private MapGeometry convert(String experimentId, String team, String json) {
        Object object = JsonUtils.read(json, Object.class);
        try {
            MapGeometry entity = new MapGeometry();
            entity.setExperimentId(experimentId);
            entity.setTeam(team);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(JsonUtils.write(object));
            entity.setId(node.findValue("id").asText());
            entity.setName(node.findValue("name").asText());
            JsonNode areaType = node.findValue("areaType");
            if (areaType != null && !StringUtils.isBlank(areaType.asText())) {
                for (GeometryFunctionType typeEnum : GeometryFunctionType.values()) {
                    if (typeEnum.getValue().equals(areaType)) {
                        entity.setType(typeEnum);
                        break;
                    }
                }
            }

            JsonNode geometryNode = node.findValue("geometry");
            String geometryType = geometryNode.findValue("type").asText();
            if (StringUtils.isBlank(geometryType)) {
                throw new SoulBootException(TinderErrorCode.TINDER_GEOMETRY_ERROR, "geometry.type 不能为空");
            }
            entity.setGeometryType(geometryType);
            entity.setGeometry(JsonUtils.write(object));

            JsonNode properties = node.findValue("properties");
            String propertiesType = properties.findValue("type").asText();
            if (StringUtils.isBlank(propertiesType)) {
                throw new SoulBootException(TinderErrorCode.TINDER_GEOMETRY_ERROR, "properties.type 不能为空");
            }
            entity.setPropertiesType(propertiesType);
            entity.setCreateTime(LocalDateTime.now());
            return entity;
        } catch (Exception e) {
            throw new SoulBootException(TinderErrorCode.TINDER_GEOMETRY_ERROR, e.getMessage());
        }
    }

    private Properties getProperties(MapGeometry geometry) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(geometry.getGeometry());
            String str = node.findValue("properties").toString();
            return JsonUtils.read(str, Properties.class);
        } catch (Exception ex) {
            throw new SoulBootException(TinderErrorCode.TINDER_GEOMETRY_ERROR, ex.getMessage());
        }

    }

    private List<Double> getHeights(MapGeometry geometry) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(geometry.getGeometry());
            String str = node.findValue("heights").toString();
            return JsonUtils.readList(str, Double.class);
        } catch (Exception ex) {
            throw new SoulBootException(TinderErrorCode.TINDER_GEOMETRY_ERROR, ex.getMessage());
        }
    }

    private List<GeometryModel> convertGeometryModel(List<MapGeometry> list) {
        List<GeometryModel> result = new ArrayList<>();
        try {
            for (MapGeometry geometry : list) {
                GeometryModel model = new GeometryModel();
                if (StringUtils.equals("Point", geometry.getGeometryType())) {
                    model.setGeometryType(GeometryType.POINT);
                } else if (StringUtils.equals("LineString", geometry.getGeometryType())) {
                    model.setGeometryType(GeometryType.LINESTRING);
                } else if (StringUtils.equals("Polygon", geometry.getGeometryType()) && StringUtils.equals("Polygon", geometry.getPropertiesType())) {
                    model.setGeometryType(GeometryType.POLYGON);
                } else if (StringUtils.equals("Polygon", geometry.getGeometryType()) && StringUtils.equals("Circle", geometry.getPropertiesType())) {
                    model.setGeometryType(GeometryType.CIRCLE);
                } else {
                    continue;
                }
                Properties properties = getProperties(geometry);

                if (properties == null) {
                    continue;
                }
                List<Double> heights = getHeights(geometry);
                if (heights == null) {
                    continue;
                }
                model.setStyle(properties.getStyle());
                if (StringUtils.equals("Polygon", geometry.getGeometryType()) && StringUtils.equals("Circle", geometry.getPropertiesType())) {
                    Double[][] points = properties.getPoints();
                    Double distance = GeometryUtils.getDistance(points[0][0], points[0][1], points[1][0], points[1][1]);
                    Double[][] center = new Double[1][3];
                    center[0][0] = points[0][0];
                    center[0][1] = points[0][1];
                    center[0][2] = heights.get(0);
                    model.setPoints(center);
                    model.setAdditional(distance);
                } else {
                    int length = properties.getPoints().length;
                    Double[][] points = new Double[length][3];
                    for (int i = 0; i < length; i++) {
                        points[i][0] = properties.getPoints()[i][0];
                        points[i][1] = properties.getPoints()[i][1];
                        points[i][2] = heights.get(i);
                    }
                    model.setPoints(points);
                }
                model.setId(geometry.getId());
                if (geometry.getType() == null) {
                    model.setType(GeometryFunctionType.OTHER.getValue());
                } else {
                    model.setType(geometry.getType().getValue());
                }

                model.setName(geometry.getName());
                result.add(model);
            }
        } catch (Exception ex) {
            throw new SoulBootException(TinderErrorCode.TINDER_GEOMETRY_ERROR, ex.getMessage());
        }
        return result;
    }

}
