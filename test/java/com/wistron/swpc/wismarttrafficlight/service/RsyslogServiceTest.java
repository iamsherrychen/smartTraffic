package com.wistron.swpc.wismarttrafficlight.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class RsyslogServiceTest {

    @MockBean
    IntersectionService intersectionService;

    @Autowired
    RsyslogService rsyslogService;

    @Test
    void log() {
        rsyslogService.log("unit test");
    }
}