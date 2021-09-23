package com.wistron.swpc.wismarttrafficlight.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.wistron.swpc.wismarttrafficlight.entity.Intersection;
import com.wistron.swpc.wismarttrafficlight.mapper.IntersectionMapper;
import com.wistron.swpc.wismarttrafficlight.vo.RoadCoordinateVO;
import com.wistron.swpc.wismarttrafficlight.vo.RoadPointVO;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RoadCoordinateService {

    private static Logger logger = LoggerFactory.getLogger(RoadCoordinateService.class);

    @Autowired
    private IntersectionMapper intersectionDao;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${api.base_url_route_api}")
    private String baseUrlRouteApi;

    @Value("${api.base_uri_openstreetmap_api}")
    private String baseUriOpenStreetMapApi;

    private static final String ROUTE_PATH_KEY = "route_path:";

    private JsonMapper jsonMapper = new JsonMapper();

    /**
     * 处理连接路口路径
     * @param intersectionId
     * @param connectIntersectionId
     * @return
     */
    public List<RoadPointVO> processRoutePath(String intersectionId, String connectIntersectionId, boolean isTask) {

        String connectPathKey = ROUTE_PATH_KEY + intersectionId + "_" + connectIntersectionId;
        // 定时任务将自动覆盖地图路径数据
        if (!isTask) {
            String roadPathStr = stringRedisTemplate.opsForValue().get(connectPathKey);

            if (!StringUtils.isEmpty(roadPathStr)) {
                try {
                    return jsonMapper.readValue(roadPathStr, new TypeReference<List<RoadPointVO>>() {});
                } catch (JsonProcessingException e) {
                    logger.error("[traffic] parse roadPathStr error", e);
                }
            }
        }

        List<RoadPointVO> roadPathList = new ArrayList<>();

        Intersection startIntersection = intersectionDao.selectById(intersectionId);
        RoadPointVO startCoordinate = new RoadPointVO();
        startCoordinate.setLatitude(startIntersection.getCenterLatitude());
        startCoordinate.setLongitude(startIntersection.getCenterLongitude());

        Intersection endIntersection = intersectionDao.selectById(connectIntersectionId);
        RoadPointVO endCoordinate = new RoadPointVO();
        endCoordinate.setLatitude(endIntersection.getCenterLatitude());
        endCoordinate.setLongitude(endIntersection.getCenterLongitude());

        StringBuilder routeApiUrl = new StringBuilder();
        routeApiUrl.append(baseUrlRouteApi)
                .append("/route/v1/car/")
                .append(startCoordinate.getLongitude()).append(",")
                .append(startCoordinate.getLatitude()).append(";")
                .append(endCoordinate.getLongitude()).append(",")
                .append(endCoordinate.getLatitude())
                .append("?overview=full&geometries=geojson&annotations=true");

        try {
            ResponseEntity<Map> routeApiResponse = restTemplate.getForEntity(routeApiUrl.toString(), Map.class);

            JSONObject rootNode = new JSONObject(routeApiResponse.getBody());

            JSONArray routeArr = rootNode.getJSONArray("routes");
            JSONObject routeFirst = routeArr.getJSONObject(0);
            JSONArray legsArr = routeFirst.getJSONArray("legs");
            JSONObject legsFirst = legsArr.getJSONObject(0);

            JSONArray nodesArr = legsFirst.getJSONObject("annotation").getJSONArray("nodes");

            JSONArray lastNodeWaysArr = null;
            StringBuilder apiUrl = new StringBuilder();
            for (int i = 0; i < nodesArr.size(); i++) {
                apiUrl.setLength(0);
                apiUrl.append(baseUriOpenStreetMapApi)
                        .append("/api/0.6/node/")
                        .append(nodesArr.getString(i))
                        .append("/ways");

                ResponseEntity<Map> response = restTemplate.getForEntity(apiUrl.toString(), Map.class);
                JSONObject responseNode = new JSONObject(response.getBody());
                if (null == lastNodeWaysArr) {
                    lastNodeWaysArr = responseNode.getJSONArray("elements");
                    continue;
                }

                JSONObject whichWay = null;
                JSONArray currentNodeWaysArr = responseNode.getJSONArray("elements");

                for (int j = 0; j < currentNodeWaysArr.size(); j++) {
                    String currentWayId = currentNodeWaysArr.getJSONObject(j).getString("id");
                    for (int k = 0; k < lastNodeWaysArr.size(); k++) {
                        if (currentWayId.equals(lastNodeWaysArr.getJSONObject(k).getString("id"))) {
                            whichWay = currentNodeWaysArr.getJSONObject(j);
                            break;
                        }
                    }
                }

                JSONArray coordinatesArr = routeFirst.getJSONObject("geometry").getJSONArray("coordinates");

                double currentNodeLatitude = coordinatesArr.getJSONArray(i).getDouble(1);
                double currentNodeLongitude = coordinatesArr.getJSONArray(i).getDouble(0);
                double lastNodeLatitude = coordinatesArr.getJSONArray(i - 1).getDouble(1);
                double lastNodeLongitude = coordinatesArr.getJSONArray(i - 1).getDouble(0);

                if (whichWay.getJSONObject("tags").containsKey("oneway")
                        && "yes".equals(whichWay.getJSONObject("tags").getString("oneway"))) {
                    if (i == 1) {
                        roadPathList.add(new RoadPointVO(lastNodeLatitude, lastNodeLongitude));
                    }
                    roadPathList.add(new RoadPointVO(currentNodeLatitude, currentNodeLongitude));
                } else {
                    RoadCoordinateVO roadCoordinateVO = createNewCoordinateForTwoWayRoad(lastNodeLatitude,
                            lastNodeLongitude, currentNodeLatitude, currentNodeLongitude);
                    if (i == 1) {
                        roadPathList.add(new RoadPointVO(roadCoordinateVO.getStartLatitude(), roadCoordinateVO.getStartLongitude()));
                    }
                    roadPathList.add(new RoadPointVO(roadCoordinateVO.getEndLatitude(), roadCoordinateVO.getEndLongitude()));
                }
                lastNodeWaysArr = responseNode.getJSONArray("elements");
            }

        } catch (JSONException e) {
            RoadCoordinateVO roadCoordinateVO = createNewCoordinateForTwoWayRoad(startCoordinate.getLatitude(),
                    startCoordinate.getLongitude(), endCoordinate.getLatitude(), endCoordinate.getLongitude());
            roadPathList.add(new RoadPointVO(roadCoordinateVO.getStartLatitude(), roadCoordinateVO.getStartLongitude()));
            roadPathList.add(new RoadPointVO(roadCoordinateVO.getEndLatitude(), roadCoordinateVO.getEndLongitude()));
        }

        try {
            stringRedisTemplate.opsForValue().set(connectPathKey, jsonMapper.writeValueAsString(roadPathList), 1, TimeUnit.DAYS);
        } catch (JsonProcessingException e) {
            logger.error("[traffic] cache roadPath error", e);
        }

        return roadPathList;
    }

    /**
     * 计算路径偏移量，生成新的路径点
     * @param startLatitude
     * @param startLongitude
     * @param endLatitude
     * @param endLongitude
     * @return
     */
    private RoadCoordinateVO createNewCoordinateForTwoWayRoad(double startLatitude, double startLongitude,
                                                              double endLatitude, double endLongitude) {
        double shiftFact = 0.00004;

        RealMatrix start = new Array2DRowRealMatrix(new double[]{startLongitude, startLatitude});
        RealMatrix end = new Array2DRowRealMatrix(new double[]{endLongitude, endLatitude});

        double angle = 90d;

        double theta = angle / 180 * Math.PI;
        double[] thetaStart = {Math.cos(theta), Math.sin(theta)};
        double[] thetaEnd = {-Math.sin(theta), Math.cos(theta)};
        RealMatrix rotationMatrix = new Array2DRowRealMatrix(new double[][]{thetaStart, thetaEnd});

        RealMatrix vec = end.subtract(start);
        RealMatrix vecNorm = vec.multiply(new Array2DRowRealMatrix(new double[]{1 / vec.getFrobeniusNorm()}));

        RealMatrix shiftVec = rotationMatrix.multiply(vecNorm).multiply(new Array2DRowRealMatrix(new double[]{shiftFact}));

        RealMatrix newStart = start.add(shiftVec);
        RealMatrix newEnd = end.add(shiftVec);

        RoadCoordinateVO roadCoordinateVO = new RoadCoordinateVO();
        roadCoordinateVO.setStartLongitude(newStart.getData()[0][0]);
        roadCoordinateVO.setStartLatitude(newStart.getData()[1][0]);
        roadCoordinateVO.setEndLongitude(newEnd.getData()[0][0]);
        roadCoordinateVO.setEndLatitude(newEnd.getData()[1][0]);

        return roadCoordinateVO;

    }
}
