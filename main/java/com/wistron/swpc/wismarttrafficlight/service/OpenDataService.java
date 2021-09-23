package com.wistron.swpc.wismarttrafficlight.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.wistron.swpc.wismarttrafficlight.dto.NewTravelTimeRecordDTO;
import com.wistron.swpc.wismarttrafficlight.dto.NewTravelTimeRecordFlowDTO;
import com.wistron.swpc.wismarttrafficlight.dto.TravelTimeRecordDTO;
import com.wistron.swpc.wismarttrafficlight.vo.AreaStatisticVO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

@Service
public class OpenDataService {

    private static Logger logger = LoggerFactory.getLogger(OpenDataService.class);

    @Value("${ids.dayuan.east_id}")
    private String travelTimeEastId;

    @Value("${ids.dayuan.west_id}")
    private String travelTimeWestId;

    // 大竹案 - 大竹中正東路直行
    @Value("${ids.dazu.zzer_f_id}")
    private String travelTimeDazuZzerForwardId;

    @Value("${ids.dazu.zzer_r_id}")
    private String travelTimeDazuZzerReverseId;

    // 大竹案 - 台31線上下匝道
    @Value("${ids.dazu.nqr_f_id}")
    private String travelTimeDazuNqrForwardId;

    @Value("${ids.dazu.nqr_r_id_1}")
    private String travelTimeDazuNqrReverseId_1;

    @Value("${ids.dazu.nqr_r_id_2}")
    private String travelTimeDazuNqrReverseId_2;

    // 大竹案 - 台31線與中正東路正反向轉彎
    @Value("${ids.dazu.nqr_turn_zzer_f_id}")
    private String travelTimeDazuNqrTurnZzerForwardId;

    @Value("${ids.dazu.nqr_turn_zzer_r_id}")
    private String travelTimeDazuNqrTurnZzerReverseId;

    @Value("${api.travel_time}")
    private String travelTimeUrl;

    @Value("${api.travel_time_xml}")
    private String travelTimeXmlUrl;

    @Value("${api.travel_time_2}")
    private String newTravelTimeUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    private JsonMapper jsonMapper = new JsonMapper();

    private String xmlTagName = "Info";
    private String xmlIdItemName = "avipairid";
    private String xmlTravelTimeItemName = "traveltime";

    /**
     * 返回旅行时间，数组长度为 1,2(大園), 3,4,5,6,7,8(大竹) 
     * result[0] - 东向旅行时间 
     * result[1] - 西向旅行时间
     * result[2] - 大竹中正東路直行旅行時間(順向) 
     * result[3] - 大竹中正東路直行旅行時間(逆向) 
     * result[4] - 台31線上下匝道旅行時間(順向) 
     * result[5] - 台31線上下匝道旅行時間(逆向) 
     * result[6] - 台31線與中正東路正反向轉彎旅行時間(順向) 
     * result[7] - 台31線與中正東路正反向轉彎旅行時間(逆向)
     * 
     * @return
     */
    public int[] getTravelTime() {
        int[] result = { -1, -1, -1, -1, -1, -1, -1, -1 };
        AreaStatisticVO lastResult = new AreaStatisticVO();

        String travelTimeStr = stringRedisTemplate.opsForValue().get("travel_time");

        if (!StringUtils.isEmpty(travelTimeStr)) {
            try {
                lastResult = jsonMapper.readValue(travelTimeStr, AreaStatisticVO.class);
            } catch (JsonMappingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JsonProcessingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // int[] travelList = queryOpenDataByXml();
        int[] newTravelList = secondTypeQueryOpenData();
        // if(travelList == null) {
        //     travelList = queryOpenData(400, 0);
        // }

        // result[0] = travelList[0];
        // result[1] = travelList[1];

        if(null != newTravelList) {
            if (lastResult.getTravelTimeEast() != null && lastResult.getTravelTimeEast() > 0) {
                result[0] = newTravelList[0] <= 0 ? lastResult.getTravelTimeEast() : newTravelList[0];
            } else {
                result[0] = newTravelList[0] <= 0 ? 0 : newTravelList[0];
            }
            if (lastResult.getTravelTimeWest() != null && lastResult.getTravelTimeWest() > 0) {
                result[1] = newTravelList[1] <= 0 ? lastResult.getTravelTimeWest() : newTravelList[1];
            } else {
                result[1] = newTravelList[1] <= 0 ? 0 : newTravelList[1];
            }
            if (lastResult.getTravelTimeDazuZzerForward() != null && lastResult.getTravelTimeDazuZzerForward() > 0) {
                result[2] = newTravelList[2] <= 0 ? lastResult.getTravelTimeDazuZzerForward() : newTravelList[2];
            } else {
                result[2] = newTravelList[2] <= 0 ? 0 : newTravelList[2];
            }
            if (lastResult.getTravelTimeDazuZzerReverse() != null && lastResult.getTravelTimeDazuZzerReverse() > 0) {
                result[3] = newTravelList[3] <= 0 ? lastResult.getTravelTimeDazuZzerReverse() : newTravelList[3];
            } else {
                result[3] = newTravelList[3] <= 0 ? 0 : newTravelList[3];
            }
            if (lastResult.getTravelTimeDazuNqrForward() != null && lastResult.getTravelTimeDazuNqrForward() > 0) {
                result[4] = newTravelList[4] <= 0 ? lastResult.getTravelTimeDazuNqrForward() : newTravelList[4];
            } else {
                result[4] = newTravelList[4] <= 0 ? newTravelList[0] : newTravelList[4];
            }
            if (lastResult.getTravelTimeDazuNqrReverse() != null && lastResult.getTravelTimeDazuNqrReverse() > 0) {
                result[5] = newTravelList[5] <= 0 ? lastResult.getTravelTimeDazuNqrReverse() : newTravelList[5];
            } else {
                result[5] = newTravelList[5] <= newTravelList[1] ? 0 : newTravelList[5];
            }
            if (lastResult.getTravelTimeDazuNqrTurnZzerForward() != null && lastResult.getTravelTimeDazuNqrTurnZzerForward() > 0) {
                result[6] = newTravelList[6] <= 0 ? lastResult.getTravelTimeDazuNqrTurnZzerForward() : newTravelList[6];
            } else {
                result[6] = newTravelList[6] <= newTravelList[3] ? 0 : newTravelList[6];
            }
            if (lastResult.getTravelTimeDazuNqrTurnZzerReverse() != null && lastResult.getTravelTimeDazuNqrTurnZzerReverse() > 0) {
                result[7] = newTravelList[7] <= 0 ? lastResult.getTravelTimeDazuNqrTurnZzerReverse() : newTravelList[7];
            } else {
                result[7] = newTravelList[7] <= newTravelList[2] ? 0 : newTravelList[7];
            }
            // result[0] = newTravelList[0] <= 0 ? lastResult.getTravelTimeEast() : newTravelList[0];
            // result[1] = newTravelList[1] <= 0 ? lastResult.getTravelTimeWest() : newTravelList[1];
            // result[2] = newTravelList[2] <= 0 ? lastResult.getTravelTimeDazuZzerForward() : newTravelList[2];
            // result[3] = newTravelList[3] <= 0 ? lastResult.getTravelTimeDazuZzerReverse() : newTravelList[3];
            // result[4] = newTravelList[4] <= 0 ? lastResult.getTravelTimeDazuNqrForward() : newTravelList[4];
            // result[5] = newTravelList[5] <= 0 ? lastResult.getTravelTimeDazuNqrReverse() : newTravelList[5];
            // result[6] = newTravelList[6] <= 0 ? lastResult.getTravelTimeDazuNqrTurnZzerForward() : newTravelList[6];
            // result[7] = newTravelList[7] <= 0 ? lastResult.getTravelTimeDazuNqrTurnZzerReverse() : newTravelList[7];
            // for(int i = 0 ; i < result.length ; i++) {
            //     result[i] = newTravelList[i] <= 0 ? 0 : newTravelList[i];
            // }
                result[4]  = (int) (result[0] * 0.9);
            
                result[5]  = result[1];
            
                result[6]  = (int) (result[3] * 1.3);
            
                result[7]  = (int) (result[2] * 1.1);
            
        }
        
        return result;
    }

    /**
     * 查询 open data
     * @param limit
     * @param offset
     * @return
     */
    public int[] queryOpenData(int limit, int offset) {
        try {

            String queryUrl = travelTimeUrl + "&limit=" + limit + "&offset=" + offset;
            ResponseEntity<String> response = restTemplate.getForEntity(queryUrl, String.class);

            if (response.getStatusCodeValue() == 200
                    && null != response.getBody()) {

                JsonNode rootNode = objectMapper.readTree(response.getBody());

                if ("true".equals(rootNode.get("success").toString())
                        && !StringUtils.isEmpty(rootNode.get("result").toString())) {

                    String records = rootNode.get("result").get("records").toString();
                    List<TravelTimeRecordDTO> data = objectMapper.readValue(records, new TypeReference<List<TravelTimeRecordDTO>>() {
                    });

                    //大園
                    int travelTimeWest = -1;
                    int travelTimeEast = -1;

                    for (TravelTimeRecordDTO record : data) {

                        //大園
                        if (travelTimeEastId.equals(record.getAvipairid())) {
                            travelTimeEast = Integer.parseInt(record.getTraveltime());
                            if(travelTimeEast < 0) {
                                return null;
                            }
                        }
                        if (travelTimeWestId.equals(record.getAvipairid())) {
                            travelTimeWest = Integer.parseInt(record.getTraveltime());
                            if(travelTimeWest < 0) {
                                return null;
                            }
                        }
                    }
                }

                // 若没有查询到数据则继续查询，直到查询到数据或查询到总条数结束
                int total = Integer.parseInt(rootNode.get("result").get("total").toString());
                int currentOffset = Integer.parseInt(rootNode.get("result").get("offset").toString());

                if ((currentOffset + limit) < total) {
                    return queryOpenData(limit, currentOffset + limit);
                }
            }
        } catch (Exception ex) {
            return new int[]{0, 0};
            // logger.error("[traffic] getTravelTime fail ", ex);
        }

        // if error, return null
        return null;
    }

    private int[] queryOpenDataByXml() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder db = factory.newDocumentBuilder();
            Document doc = (Document) db.parse(new URL(travelTimeXmlUrl).openStream());
            NodeList idList = doc.getElementsByTagName(xmlTagName);
            
            //大園
            int travelTimeWest = -1;
            int travelTimeEast = -1;

            for(int i =0;i<idList.getLength();i++) {
                Node node = idList.item(i);
                NamedNodeMap nameNode = node.getAttributes();
                String id = nameNode.getNamedItem(xmlIdItemName).getNodeValue();

                //大園
                if(travelTimeEastId.equals(id)) {
                    travelTimeEast = Integer.parseInt(nameNode.getNamedItem(xmlTravelTimeItemName).getNodeValue());
                }

                if(travelTimeWestId.equals(id)) {
                    travelTimeWest = Integer.parseInt(nameNode.getNamedItem(xmlTravelTimeItemName).getNodeValue());
                }

            }

            if (travelTimeEast != -1 && travelTimeWest != -1) {
                return new int[]{travelTimeEast, travelTimeWest};
            }
        } catch (Exception e) {
            return new int[]{0, 0};
            // logger.error("[traffic] getTravelTime xml fail ", e);
        }

        return null;
    }

    public int[] secondTypeQueryOpenData() {
        try {

            String queryUrl = newTravelTimeUrl;
            ResponseEntity<String> response = restTemplate.getForEntity(queryUrl, String.class);

            if (response.getStatusCodeValue() == 200
                    && null != response.getBody()) {

                JsonNode rootNode = objectMapper.readTree(response.getBody());

                    String records = rootNode.toString();
                    List<NewTravelTimeRecordDTO> data = objectMapper.readValue(records, new TypeReference<List<NewTravelTimeRecordDTO>>() {
                    });

                    //大園
                    int travelTimeWest = -1;
                    int travelTimeEast = -1;
        
                    //大竹
                    int travelTimeDazuZzerForward = -1;
                    int travelTimeDazuZzerReverse = -1;
                    int travelTimeDazuNqrForward = -1;
                    int travelTimeDazuNqrReverse_1 = -1;
                    int travelTimeDazuNqrReverse_2 = -1;
                    int travelTimeDazuNqrTurnZzerForward = -1;
                    int travelTimeDazuNqrTurnZzerReverse = -1;

                    for (NewTravelTimeRecordDTO record : data) {

                        //大竹
                        if (travelTimeEastId.equals(record.getETagPairID())) {
                            int tempLength = 0;
                            for(int i = 0; i < record.getFlows().length; i++) {
                                if(record.getFlows()[i].getTravelTime() > 0) {
                                    travelTimeEast += record.getFlows()[i].getTravelTime();
                                    tempLength++;
                                }
                            }
                            travelTimeEast = tempLength > 0 ? travelTimeEast / tempLength : 0;
                        }            
                        if (travelTimeWestId.equals(record.getETagPairID())) {
                            int tempLength = 0;
                            for(int i = 0; i < record.getFlows().length; i++) {
                                if(record.getFlows()[i].getTravelTime() > 0) {
                                    travelTimeWest += record.getFlows()[i].getTravelTime();
                                    tempLength++;
                                }
                            }
                            travelTimeWest = tempLength > 0 ? travelTimeWest / tempLength : 0;
                        }
                        
                        //大竹
                        if (travelTimeDazuZzerForwardId.equals(record.getETagPairID())) {
                            int tempLength = 0;
                            for(int i = 0; i < record.getFlows().length; i++) {
                                if(record.getFlows()[i].getTravelTime() > 0) {
                                    travelTimeDazuZzerForward += record.getFlows()[i].getTravelTime();
                                    tempLength++;
                                }
                            }
                            travelTimeDazuZzerForward = tempLength > 0 ? travelTimeDazuZzerForward / tempLength : 0;
                        }
                        if (travelTimeDazuZzerReverseId.equals(record.getETagPairID())) {
                            int tempLength = 0;
                            for(int i = 0; i < record.getFlows().length; i++) {
                                if(record.getFlows()[i].getTravelTime() > 0) {
                                    travelTimeDazuZzerReverse += record.getFlows()[i].getTravelTime();
                                    tempLength++;
                                }
                            }
                            travelTimeDazuZzerReverse = tempLength > 0 ? travelTimeDazuZzerReverse / tempLength : 0;
                        }

                        if (travelTimeDazuNqrForwardId.equals(record.getETagPairID())) {
                            int tempLength = 0;
                            for(int i = 0; i < record.getFlows().length; i++) {
                                if(record.getFlows()[i].getTravelTime() > 0) {
                                    travelTimeDazuNqrForward += record.getFlows()[i].getTravelTime();
                                    tempLength++;
                                }
                            }
                            travelTimeDazuNqrForward = tempLength > 0 ? travelTimeDazuNqrForward / tempLength : 0;
                        }

                        if (travelTimeDazuNqrReverseId_1.equals(record.getETagPairID())) {
                            int tempLength = 0;
                            for(int i = 0; i < record.getFlows().length; i++) {
                                if(record.getFlows()[i].getTravelTime() > 0) {
                                    travelTimeDazuNqrReverse_1 += record.getFlows()[i].getTravelTime();
                                    tempLength++;
                                }
                            }
                            travelTimeDazuNqrReverse_1 = tempLength > 0 ? travelTimeDazuNqrReverse_1 / tempLength : 0;
                        }

                        if (travelTimeDazuNqrReverseId_2.equals(record.getETagPairID())) {
                            int tempLength = 0;
                            for(int i = 0; i < record.getFlows().length; i++) {
                                if(record.getFlows()[i].getTravelTime() > 0) {
                                    travelTimeDazuNqrReverse_2 += record.getFlows()[i].getTravelTime();
                                    tempLength++;
                                }
                            }
                            travelTimeDazuNqrReverse_2 = tempLength > 0 ? travelTimeDazuNqrReverse_2 / tempLength : 0;
                        }

                        if (travelTimeDazuNqrTurnZzerForwardId.equals(record.getETagPairID())) {
                            int tempLength = 0;
                            for(int i = 0; i < record.getFlows().length; i++) {
                                if(record.getFlows()[i].getTravelTime() > 0) {
                                    travelTimeDazuNqrTurnZzerForward += record.getFlows()[i].getTravelTime();
                                    tempLength++;
                                }
                            }
                            travelTimeDazuNqrTurnZzerForward = tempLength > 0 ? travelTimeDazuNqrTurnZzerForward / tempLength : 0;
                        }
                        
                        if (travelTimeDazuNqrTurnZzerReverseId.equals(record.getETagPairID())) {
                            int tempLength = 0;
                            for(int i = 0; i < record.getFlows().length; i++) {
                                if(record.getFlows()[i].getTravelTime() > 0) {
                                    travelTimeDazuNqrTurnZzerReverse += record.getFlows()[i].getTravelTime();
                                    tempLength++;
                                }
                            }
                            travelTimeDazuNqrTurnZzerReverse = tempLength > 0 ? travelTimeDazuNqrTurnZzerReverse / tempLength : 0;
                        }
                    }
                    return new int[]{travelTimeEast, travelTimeWest, travelTimeDazuZzerForward, travelTimeDazuZzerReverse, travelTimeDazuNqrForward, (travelTimeDazuNqrReverse_1 + travelTimeDazuNqrReverse_2)/2, travelTimeDazuNqrTurnZzerForward, travelTimeDazuNqrTurnZzerReverse};
                }

        } catch (Exception ex) {
            logger.error("[traffic] getTravelTime fail ", ex);
        }

        // if error, return null
        return null;
    }
}
