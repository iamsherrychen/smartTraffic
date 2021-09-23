package com.wistron.swpc.wismarttrafficlight.controller;

import com.wistron.swpc.wismarttrafficlight.exception.ValidateException;
import com.wistron.swpc.wismarttrafficlight.service.HistoryRecordService;
import com.wistron.swpc.wismarttrafficlight.util.DateUtil;
import com.wistron.swpc.wismarttrafficlight.vo.RecordDownloadVO;
import com.wistron.swpc.wismarttrafficlight.vo.TrafficResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/history")
public class HistoryRecordController {

    private static Logger logger = LoggerFactory.getLogger(HistoryRecordController.class);

    @Autowired
    private HistoryRecordService historyRecordService;

    /**
     * 根据日期去 mongodb 查询流量趋势图
     * @param date
     * @return
     */
    @GetMapping(value = "/time_statistic",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public TrafficResponse search(@PathParam("date") String date) {

        if (!(DateUtil.isLegalDate(date) && DateUtil.validateSearchDate(date, 0, 14))) {
            throw new ValidateException("Date validation failed");
        }

        return TrafficResponse.ok().setResult(historyRecordService.search(date));
    }

    /**
     * 根据日期去 mongodb 查询流量趋势图
     * @param date
     * @return
     */
    @GetMapping(value = "/time_statistic/google",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public TrafficResponse searchGoogle(@PathParam("date") String date) {

        if (!(DateUtil.isLegalDate(date) && DateUtil.validateSearchDate(date, 0, 14))) {
            throw new ValidateException("Date validation failed");
        }

        return TrafficResponse.ok().setResult(historyRecordService.searchGoogle(date));
    }

    /**
     * 根据日期去 mongodb 查询停等圖
     * @param date
     * @return
     */
    @GetMapping(value = "/car_delay",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public TrafficResponse searchCarDelay(@PathParam("date") String date) {

        if (!(DateUtil.isLegalDate(date) && DateUtil.validateSearchDate(date, 0, 14))) {
            throw new ValidateException("Date validation failed");
        }

        return TrafficResponse.ok().setResult(historyRecordService.searchCarDelay(date));
    }

    /**
     * 根据日期打包下载 rsyslog
     * @param date
     * @param resp
     */
    //New feature 針對不同檔案分開下載
    @GetMapping(value = "/download/{downloadType}",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void download(@PathVariable("downloadType") String downloadType, @PathParam("date") String date, HttpServletResponse resp) {

        if (!(DateUtil.isLegalDate(date) && DateUtil.validateSearchDate(date, 2, 0))) {
            throw new ValidateException("Date validation failed");
        }

        RecordDownloadVO recordDownloadVO = historyRecordService.download(downloadType, date);
        File zipFile = recordDownloadVO.getZipFile();

        resp.setContentType("application/force-download");
        resp.addHeader("Content-Disposition", "attachment;fileName=" + date + "_"+ downloadType + ".zip");
        resp.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        resp.addHeader("Pragma", "no-cache");
        resp.addHeader("Expires", "0");
        resp.addHeader("X-Content-Type-Options", "nosniff");
        resp.addHeader("X-Frame-Options", "DENY");
        resp.addHeader("X-XSS-Protection", "1; mode=block");

        if (null == zipFile || !zipFile.exists()) {
            return;
        }

        FileInputStream fis = null;
        ServletOutputStream sos = null;

        try {
            int len;
            byte[] buf = new byte[1024];
            fis = new FileInputStream(zipFile);
            sos = resp.getOutputStream();
            while ((len = fis.read(buf)) > 0) {
                sos.write(buf, 0, len);
            }
            fis.close();
            sos.flush();
            if (!recordDownloadVO.getHasArchive()) {
                zipFile.delete();
            }
        } catch (IOException e) {
            logger.error("[traffic] download file error", e);
        } finally {
            if(fis!=null)
                safeCloseInputStream(fis);
            if(sos!=null)
                safeCloseOutputStream(sos);
            
        }

    }

    private void safeCloseOutputStream(@NonNull final ServletOutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void safeCloseInputStream(@NonNull final FileInputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
