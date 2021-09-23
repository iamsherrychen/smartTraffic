package com.wistron.swpc.wismarttrafficlight.service;

import com.wistron.swpc.wismarttrafficlight.helper.TrafficCarDelayDataHelper;
import com.wistron.swpc.wismarttrafficlight.helper.TrafficFlowDataHelper;
import com.wistron.swpc.wismarttrafficlight.vo.RecordDownloadVO;
import com.wistron.swpc.wismarttrafficlight.vo.StatisticDataVO;
import com.wistron.swpc.wismarttrafficlight.vo.TimeStatisticVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class HistoryRecordService {

    private static Logger logger = LoggerFactory.getLogger(HistoryRecordService.class);

    private static final String RSYSLOG_PATH = "rsyslog";

    private static final String RSYSLOG_ARCHIVE = "rsyslog" + File.separator + "archive";

    private static final String ARCHIVE_SUFFIX = ".zip";

    @Autowired
    private TrafficFlowDataHelper trafficFlowDataHelper;
    @Autowired
    private TrafficCarDelayDataHelper trafficCarDelayDataHelper;
    @Autowired
    private GoolgeService googleService;

    public RecordDownloadVO download(String downloadType, String searchDate) {

        RecordDownloadVO recordDownloadVO = new RecordDownloadVO();

        File dir = new File(RSYSLOG_PATH);
        File archiveDir = new File(RSYSLOG_ARCHIVE);

        if (!dir.exists() || !dir.isDirectory()) {
            return recordDownloadVO;
        }

        if (archiveDir.exists() && archiveDir.isDirectory()) {
            String archiveFileName = searchDate.trim() + "_" + downloadType + ARCHIVE_SUFFIX;
            String archiveFileNameOrg = searchDate.trim() + ARCHIVE_SUFFIX;
            File[] zipFiles = archiveDir.listFiles((fileDir, name) -> archiveFileName.equals(name) || archiveFileNameOrg.equals(name));

            if (zipFiles.length > 0) {
                recordDownloadVO.setZipFile(zipFiles[0]);
                recordDownloadVO.setHasArchive(true);
                return recordDownloadVO;
            }
        }

        File[] files = null;

        switch (downloadType) {
            case "timeslot_status":
                files = dir.listFiles((fileDir, name) -> name.contains("tc_edge")||name.contains("exe"));
                break;

            case "flow":
                files = dir.listFiles((fileDir, name) -> name.contains("csv")||name.contains("webserver")||name.contains("tc_host2")||name.contains("SC_Agent")||name.contains("IPCMSG"));
                break;
        
            default:
                files = dir.listFiles((fileDir, name) -> name.contains("log")||name.contains("exe")||name.contains("csv"));
                break;
        }

        recordDownloadVO.setHasArchive(false);
        recordDownloadVO.setZipFile(compressZip(files, UUID.randomUUID().toString()));

        return recordDownloadVO;
    }

    public List<TimeStatisticVO<List<StatisticDataVO>>> search(String searchDateStr) {
        return trafficFlowDataHelper.processTrafficTrend(null, searchDateStr);
    }

    public List<TimeStatisticVO> searchGoogle(String searchDateStr) {
        return googleService.processTrafficTrend(searchDateStr);
    }

    public List<TimeStatisticVO<List<StatisticDataVO>>> searchCarDelay(String searchDateStr) {
        return trafficCarDelayDataHelper.processCarDelay(null, searchDateStr);
    }

    private File compressZip(File[] files, String zipFilePath) {
        byte[] buf = new byte[1024];
        File zipFile = new File(zipFilePath);

        ZipOutputStream zos = null;
        FileInputStream fis = null;

        try {
            if (!zipFile.exists()) {
                if (!zipFile.createNewFile()) {
                    logger.error("[traffic] create file error");
                    return zipFile;
                }
            }

            zos = new ZipOutputStream(new FileOutputStream(zipFile));
            for (File file : files) {
                if (null == file || !file.exists()) {
                    continue;
                }

                fis = new FileInputStream(file);
                zos.putNextEntry(new ZipEntry(file.getName()));

                int len;
                while ((len = fis.read(buf)) > 0) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                fis.close();
            }

            // zos.close();

        } catch (IOException e) {
            logger.error("[traffic] create file error");
        } finally {
            if(zos!=null)
                safeCloseOutputStream(zos);
            if(fis!=null)
                safeCloseInputStream(fis);
        }

        return zipFile;
    }

    private void safeCloseOutputStream(@NonNull final ZipOutputStream os) {
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
