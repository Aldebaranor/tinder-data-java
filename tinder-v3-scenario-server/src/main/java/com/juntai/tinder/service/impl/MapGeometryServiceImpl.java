package com.juntai.tinder.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juntai.soulboot.data.Pagination;
import com.juntai.soulboot.data.Query;
import com.juntai.tinder.condition.MapGeometryCondition;
import com.juntai.tinder.entity.MapGeometry;
import com.juntai.tinder.mapper.MapGeometryMapper;
import com.juntai.tinder.model.GeometryModel;
import com.juntai.tinder.service.MapGeometryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
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
public class MapGeometryServiceImpl  implements MapGeometryService {

    @Override
    public List<MapGeometry> getByExperiment(String experimentId, String team) {
        //Clause clause;
        CombineClause clause = CombineClause.and(
                SingleClause.equal("experimentId", experimentId)
        );
        //白方可以查询到所有，红方查询到白方与红方，蓝方查询到白方与蓝方
        if (StringUtils.equals(TeamType.RED.getValue(), team)) {
            clause.add(SingleClause.in("team", new String[]{TeamType.RED.getValue(), TeamType.WHITE.getValue()}));
        } else if (StringUtils.equals(TeamType.BLUE.getValue(), team)) {
            clause.add(SingleClause.in("team", new String[]{TeamType.BLUE.getValue(), TeamType.WHITE.getValue()}));
        }
        return repository.query(clause);
    }

    @Override
    public List<GeometryModel> getGeometryModelByExperiment(String experimentId, String team) {
        List<MapGeometry> byExperiment = getByExperiment(experimentId, team);
        return convertGeometryModel(byExperiment);
    }

    @Override
    public List<MapGeometry> query(MapGeometryCondition condition) {
        return super.query(condition, null);;
    }

    @Override
    public Pagination<MapGeometry> page(Query<MapGeometryCondition, MapGeometry> mode) {
        return super.page(mode.getCondition(), mode.getPaging(), null);
    }

    @Override
    public MapGeometry getById(String id) {
        return null;
    }

    @Override
    public String insert(MapGeometry entity) {
        entity.setId(UUIDUtils.getHashUuid());
        entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return super.insert(entity);
    }

    @Override
    public void insertList(List<MapGeometry> entity) {
        List<MapGeometry> list = new ArrayList<>();
        List<Object> jsonList = JsonUtils.deserializeList(json, Object.class);
        jsonList.forEach(s -> {
            try {
                MapGeometry entity = new MapGeometry();
                entity.setExperimentId(experimentId);
                entity.setTeam(team);
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(JsonUtils.serialize(s));
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
                    throw ExceptionUtils.api("geometry.type 不能为空");
                }
                entity.setGeometryType(type);
                entity.setGeometry(JsonUtils.serialize(s));

                JsonNode properties = node.findValue("properties");
                String propertiesType = properties.findValue("type").asText();
                if (StringUtils.isBlank(propertiesType)) {
                    throw ExceptionUtils.api("properties.type 不能为空");
                }
                entity.setPropertiesType(propertiesType);
                entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
                list.add(entity);
            } catch (JsonProcessingException e) {
                throw ExceptionUtils.api(e.getMessage());
            }

        });
        super.insertList(list);
    }

    @Override
    public void updateListByJson(String experimentId, String team, List<String> json) {
        MapGeometryCondition condition = new MapGeometryCondition();
        condition.setTeam(team);
        condition.setExperimentId(experimentId);
        List<MapGeometry> updateList = new ArrayList<>();
        List<MapGeometry> list = super.query(condition);
        for(String s : jsonList){
            try {
                Object object = JsonUtils.deserialize(s, Object.class);
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(JsonUtils.serialize(object));
                String id = node.findValue("id").asText();
                MapGeometry mapGeometry = list.stream().filter(q -> StringUtils.equals(q.getId(), id)).findFirst().orElse(null);
                if(mapGeometry == null){
                    continue;
                }
                mapGeometry.setGeometry(JsonUtils.serialize(object));
                mapGeometry.setModifyTime(new Timestamp(System.currentTimeMillis()));
                updateList.add(mapGeometry);
            } catch (JsonProcessingException e) {
                throw ExceptionUtils.api(e.getMessage());
            }

        }
        super.updateList(updateList);
    }

    @Override
    public void saveOneByJson(String experimentId, String team, String json) {

        try {
            MapGeometry entity = convert(experimentId, team, json);
            if (super.getById(entity.getId()) == null) {
                super.insert(entity);
            } else {
                super.update(entity);
            }
        } catch (Exception e) {
            throw ExceptionUtils.api(e.getMessage());
        }
    }

    @Override
    public int deleteById(String id) {
        return 0;
    }

    @Override
    public void update(MapGeometry entity) {
        entity.setModifyTime(new Timestamp(System.currentTimeMillis()));
        super.update(entity);
    }

    @NotNull
    private MapGeometry convert(String experimentId, String team, String json) {
        Object object = JsonUtils.deserialize(json, Object.class);
        try {
            MapGeometry entity = new MapGeometry();
            entity.setExperimentId(experimentId);
            entity.setTeam(team);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(JsonUtils.serialize(object));
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
                throw ExceptionUtils.api("geometry.type 不能为空");
            }
            entity.setGeometryType(geometryType);
            entity.setGeometry(JsonUtils.serialize(object));

            JsonNode properties = node.findValue("properties");
            String propertiesType = properties.findValue("type").asText();
            if (StringUtils.isBlank(propertiesType)) {
                throw ExceptionUtils.api("properties.type 不能为空");
            }
            entity.setPropertiesType(propertiesType);
            entity.setCreateTime(new Timestamp(System.currentTimeMillis()));
            return entity;
        } catch (Exception e) {
            throw ExceptionUtils.api(e.getMessage());
        }
    }

    private Properties getProperties(MapGeometry geometry) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(geometry.getGeometry());
            String str = node.findValue("properties").toString();
            return JsonUtils.deserialize(str, Properties.class);
        } catch (Exception ex) {
            throw ExceptionUtils.api(ex.getMessage());
        }

    }

    private List<Double> getHeights(MapGeometry geometry) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(geometry.getGeometry());
            String str = node.findValue("heights").toString();
            return JsonUtils.deserializeList(str, Double.class);
        } catch (Exception ex) {
            throw ExceptionUtils.api(ex.getMessage());
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
                if(geometry.getType() == null){
                    model.setType(GeometryFunctionType.OTHER.getValue());
                }else{
                    model.setType(geometry.getType().getValue());
                }

                model.setName(geometry.getName());
                result.add(model);
            }
        } catch (Exception ex) {
            throw ExceptionUtils.api(ex.getMessage());
        }
        return result;
    }

}
