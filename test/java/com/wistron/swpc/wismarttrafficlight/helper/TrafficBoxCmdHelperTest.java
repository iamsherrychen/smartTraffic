package com.wistron.swpc.wismarttrafficlight.helper;

import com.wistron.swpc.wismarttrafficlight.constant.ParameterKeyConst;
import com.wistron.swpc.wismarttrafficlight.constant.TrafficBoxConst;
import com.wistron.swpc.wismarttrafficlight.dto.TrafficBoxMsgDTO;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
class TrafficBoxCmdHelperTest {

    @Test
    void initReqCmd() {
        Map<String, Object> params = new HashMap<>();
        params.put(ParameterKeyConst.INTERSECTION_ID, "0x0000");
        TrafficBoxMsgDTO req = TrafficBoxCmdHelper.initReqCmd(TrafficBoxConst.MSG_TYPE_GET,
                TrafficBoxConst.GET_CONTROL_STRATEGY, params);

        Assert.assertEquals(TrafficBoxConst.GET_CONTROL_STRATEGY, req.getCmd());
    }
}