package com.wistron.swpc.wismarttrafficlight.helper;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Calendar;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class WiLicenseHelperTest {

    @Test
    void checkLicence() {
        boolean flag = WiLicenseHelper.checkLicence();
        if (Calendar.getInstance().get(Calendar.MONTH) < 6) {
            Assert.assertTrue(flag);
        }
    }

}