package com.wistron.swpc.wismarttrafficlight.service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import com.wistron.swpc.wismarttrafficlight.util.DateUtil;
import com.wistron.swpc.wismarttrafficlight.exception.ValidateException;
import com.wistron.swpc.wismarttrafficlight.helper.TrafficCarDelayDataHelper;
import com.wistron.swpc.wismarttrafficlight.helper.TrafficFlowDataHelper;
import com.wistron.swpc.wismarttrafficlight.vo.StatisticDataVO;
import com.wistron.swpc.wismarttrafficlight.vo.TimeStatisticVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class CsvDownloaderService {

    @Autowired
    private TrafficFlowDataHelper trafficFlowDataHelper;
    
    @Autowired
    private TrafficCarDelayDataHelper trafficCarDelayDataHelper;
    

    public ResponseEntity<Object> downloadFile(@PathVariable("date") String date) throws IOException {

        if (!(DateUtil.isLegalDate(date) && DateUtil.validateSearchDate(date, 0, 14))) {
            throw new ValidateException("Date validation failed");
        }

        FileWriter filewriter = null;

        try {
            final String filename = "/rsyslog/"+(String)date+".csv";

            filewriter = new FileWriter(filename);
            filewriter.write(new String(new byte[] { (byte) 0xEF, (byte) 0xBB,(byte) 0xBF }));
            filewriter.write(csvCreate(date).toString());
            filewriter.flush();

            final File file = new File(filename);

            final InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            final org.springframework.http.HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");

            final ResponseEntity<Object> responseEntity = ResponseEntity.ok().headers(headers)
                    .contentLength(file.length()).contentType(MediaType.parseMediaType("application/txt"))
                    .body(resource);

            if (resource != null) {
                try {
                    resource.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
                    
            return responseEntity;
        } catch (final Exception e) {
            return new ResponseEntity<>("error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            if (filewriter != null)
                safeCloseOutputStream(filewriter);
        }
    }

    public StringBuilder csvCreate(String searchDateStr) {

        StringBuilder result = new StringBuilder();
        String timePoint = "";
        String speedEast = "";
        String travelTimeEast = "";
        String speedWest = "";
        String travelTimeWest = "";

        //大竹
        String speedDazuZzerForward = "";
        String speedDazuZzerReverse = "";
        String speedDazuNqrForward = "";
        String speedDazuNqrReverse = "";
        String speedDazuNqrTurnZzerForward = "";
        String speedDazuNqrTurnZzerReverse = "";

        String travelTimeDazuZzerForward = "";
        String travelTimeDazuZzerReverse = "";
        String travelTimeDazuNqrForward = "";
        String travelTimeDazuNqrReverse = "";
        String travelTimeDazuNqrTurnZzerForward = "";
        String travelTimeDazuNqrTurnZzerReverse = "";
        
        Map<String, String> intersectionPcuMap = new TreeMap<>();
        Map<String, String> intersectionCarDelayMap = new TreeMap<>();

        int count = 0;
        int countCarDelay = 0;
        
        for (TimeStatisticVO<List<StatisticDataVO>> i : trafficFlowDataHelper.processTrafficTrend(null, searchDateStr)) {
            timePoint += (count > 0 ? "," : "") + i.getTimePoint();
            speedEast += (count > 0 ? "," : "") + i.getSpeedEast();
            travelTimeEast += (count > 0 ? "," : "") + i.getTravelTimeEast();
            speedWest += (count > 0 ? "," : "") + i.getSpeedWest();
            travelTimeWest += (count > 0 ? "," : "") + i.getTravelTimeWest();

            speedDazuZzerForward += (count > 0 ? "," : "") + i.getSpeedDazuZzerForward();
            travelTimeDazuZzerForward += (count > 0 ? "," : "") + i.getTravelTimeDazuZzerForward();
            speedDazuZzerReverse += (count > 0 ? "," : "") + i.getSpeedDazuZzerReverse();
            travelTimeDazuZzerReverse += (count > 0 ? "," : "") + i.getTravelTimeDazuZzerReverse();

            speedDazuNqrForward += (count > 0 ? "," : "") + i.getSpeedDazuNqrForward();
            travelTimeDazuNqrForward += (count > 0 ? "," : "") + i.getTravelTimeDazuNqrForward();
            speedDazuNqrReverse += (count > 0 ? "," : "") + i.getSpeedDazuNqrReverse();
            travelTimeDazuNqrReverse += (count > 0 ? "," : "") + i.getTravelTimeDazuNqrReverse();

            speedDazuNqrTurnZzerForward += (count > 0 ? "," : "") + i.getSpeedDazuNqrTurnZzerForward();
            travelTimeDazuNqrTurnZzerForward += (count > 0 ? "," : "") + i.getTravelTimeDazuNqrTurnZzerForward();
            speedDazuNqrTurnZzerReverse += (count > 0 ? "," : "") + i.getSpeedDazuNqrTurnZzerReverse();
            travelTimeDazuNqrTurnZzerReverse += (count > 0 ? "," : "") + i.getTravelTimeDazuNqrTurnZzerReverse();

            List<StatisticDataVO> intersectionList = i.getStatisticData();
            for (StatisticDataVO j : intersectionList) {
                if(intersectionPcuMap.get(j.getIntersectionId()) == null) {
                    intersectionPcuMap.put(j.getIntersectionId(), "\n" + j.getIntersectionName() + "PCU");
                }
                String pcuStr = intersectionPcuMap.get(j.getIntersectionId());
                pcuStr += "," + j.getPcu();
                intersectionPcuMap.put(j.getIntersectionId(), pcuStr.toString());
            }
            count++;
        }

        for (TimeStatisticVO<List<StatisticDataVO>> i : trafficCarDelayDataHelper.processCarDelay(null, searchDateStr)) {

            List<StatisticDataVO> intersectionList = i.getStatisticData();
            for (StatisticDataVO j : intersectionList) {
                if(intersectionCarDelayMap.get(j.getIntersectionId()) == null) {
                    intersectionCarDelayMap.put(j.getIntersectionId(), "\n" + j.getIntersectionName() + " 停等延滯");
                }
                String carDelayStr = intersectionCarDelayMap.get(j.getIntersectionId());
                carDelayStr += "," + j.getCarDelay();
                intersectionCarDelayMap.put(j.getIntersectionId(), carDelayStr.toString());
            }
            countCarDelay++;
        }

        for(Map.Entry entry : intersectionPcuMap.entrySet()) {
            entry.getKey();
            entry.getValue();
        }

        for(Map.Entry entry : intersectionCarDelayMap.entrySet()) {
            entry.getKey();
            entry.getValue();
        }

        result.append("時間,").append(timePoint).append("\n").append("東向車速,").append(speedEast).append("\n").append("東向旅行時間,").append(travelTimeEast).append("\n").append("西向車速,").append(speedWest).append("\n").append("西向旅行時間,").append(travelTimeWest).append("\n").append("\n").append("大竹案中正東路正向車速,").append(speedDazuZzerForward).append("\n").append("大竹案中正東路正向旅行時間,").append(travelTimeDazuZzerForward).append("\n").append("大竹案中正東路反向車速,").append(speedDazuZzerReverse).append("\n").append("大竹案中正東路反向旅行時間,").append(travelTimeDazuZzerReverse).append("\n").append("\n").append("台31線上匝道車速,").append(speedDazuNqrForward).append("\n").append("台31線上匝道旅行時間,").append(travelTimeDazuNqrForward).append("\n").append("台31線下匝道車速,").append(speedDazuNqrReverse).append("\n").append("台31線下匝道旅行時間,").append(travelTimeDazuNqrReverse).append("\n").append("\n").append("台31線轉中正東路車速,").append(speedDazuNqrTurnZzerForward).append("\n").append("台31線轉中正東路旅行時間,").append(travelTimeDazuNqrTurnZzerForward).append("\n").append("中正東路轉台31線車速,").append(speedDazuNqrTurnZzerReverse).append("\n").append("中正東路轉台31線車速旅行時間,").append(travelTimeDazuNqrTurnZzerReverse).append("\n").append(intersectionPcuMap.values().toString().substring(1,intersectionPcuMap.values().toString().length()-1)).append(intersectionCarDelayMap.values().toString().substring(1,intersectionCarDelayMap.values().toString().length()-1));

        return result;
    }

    private void safeCloseOutputStream(@NonNull final FileWriter os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}