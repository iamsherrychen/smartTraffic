package com.wistron.swpc.wismarttrafficlight.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
// import com.wistron.swpc.wismarttrafficlight.util.AesEncryptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 证书验证
 */
@Component
public class WiLicenseHelper {

    private static Logger logger = LoggerFactory.getLogger(WiLicenseHelper.class);

    private static final String LICENSE_PATH = "deploy" + File.separator + "traffic.license";

    /**
     * 证书全局变量，用于标识当前证书是否可用， true - 可用, false - 不可用
     */
    public static boolean licenseStatus = true;

    private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final static String NODE_LICENSE = "license";

    private final static String NODE_DURATION = "duration";

    private final static String NODE_IS_LOCK = "isLock";

    private final static String NODE_ACTIVE_TIME = "activeTime";

    private static JsonMapper jsonMapper = new JsonMapper();

    public static String ENCODE_RULE;

    public static boolean checkLicence() {

        boolean isPassed = true;
        // boolean isPassed = false;

        // String fileContent = AesEncryptionUtil.decode(ENCODE_RULE, readerFile());

        // // 证书解析失败
        // if (null == fileContent) {
        //     return false;
        // }

        // // 验证证书时间
        // try {
        //     JsonNode licenseNode = jsonMapper.readTree(fileContent).get(NODE_LICENSE);
        //     // 获取需要验证的数据
        //     String duration = licenseNode.get(NODE_DURATION).textValue();
        //     String isLock = licenseNode.get(NODE_IS_LOCK).textValue();
        //     String licenseActiveTime = licenseNode.get(NODE_ACTIVE_TIME).textValue();

        //     // license 未锁定则进行验证
        //     if (!"1".equals(isLock)) {
        //         if (null != duration && !"".equals(duration)) {
        //             Date dt = new Date();
        //             long sysTime = dt.getTime();
        //             long activeTime = readTime(licenseActiveTime, DATE_FORMAT);
        //             long day = Long.parseLong(duration) * 86400000;
        //             // 验证 license 是否过期
        //             isPassed = (activeTime + day) >= sysTime;
        //         } else {
        //             isPassed = true;
        //         }
        //     }
        // } catch (Exception e) {
        //     logger.error("[traffic] parse license fail, {}", e.getMessage());
        //     isPassed = false;
        // }

        licenseStatus = true;

        return isPassed;
    }

    private static String readerFile() {
        File file = new File(LICENSE_PATH);
        if (!file.exists()) {
            return null;
        }
        byte[] buff = new byte[1024];
        int len;
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            len = is.read(buff);
            if (len != -1) {
                byte[] data = new byte[len];
                System.arraycopy(buff, 0, data, 0, len);
                return new String(data);
            }
        } catch (IOException e) {
            logger.error("[traffic] license error : read file error");
        } catch (NullPointerException e) {
            logger.error("[traffic] license error : not found file error");
        } catch (Exception e) {
            logger.error("[traffic] license error : file content error");
        } finally {
            if(is != null)
                safeCloseOutputStream(is);
        }
        return null;
    }

    private static long readTime(String date, String format) {
        long l = 0;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            l = sdf.parse(date).getTime();
        } catch (ParseException e) {
            logger.error("[traffic] license date format error, {}", e.getMessage());
        }
        return l;
    }

    private static void safeCloseOutputStream(@NonNull final FileInputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Value("${license.encode_rule}")
    public void setEncodeRule(String encodeRule) {
        WiLicenseHelper.ENCODE_RULE = encodeRule;
    }
}
