package com.wistron.swpc.wismarttrafficlight.helper;

import com.wistron.swpc.wismarttrafficlight.dto.TrafficBoxMsgDTO;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

public class TrafficBoxCmdHelper {

    private static Calendar calendar = Calendar.getInstance();

    public static TrafficBoxMsgDTO initReqCmd(String msgType, String cmd, Object params) {
        TrafficBoxMsgDTO req = new TrafficBoxMsgDTO();

        req.setMsgType(msgType);
        req.setCmd(cmd);
        req.setMsgSeq(getMsgSeq());
        req.setMsgTime(String.valueOf(calendar.getTimeInMillis()));

        if (Objects.isNull(params)) {
            req.setMsg(Collections.emptyList());
        } else {
            req.setMsg(Arrays.asList(params));
        }

        return req;
    }

    private static String getMsgSeq() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }

}
