package com.wistron.swpc.wismarttrafficlight.service;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class OpenDataServiceTest {

    // @MockBean
    // IntersectionService intersectionService;

    // @Value("${api.travel_time}")
    // String travelTimeUrl;

    // @MockBean
    // RestTemplate restTemplate;

    // @Autowired
    // OpenDataService openDataService;

    // @Test
    // void getTravelTime() {
    //     String openDataStr = "{\"success\":true,\"result\":{\"records\":[{\"_id\":281,\"avipairid\":\"68000108010\"," +
    //             "\"startavistatus\":\"0\",\"endavistatus\":\"0\",\"traveltime\":\"50\"," +
    //             "\"datacollecttime\":\"2020/04/30 16:01:00\"},{\"_id\":281,\"avipairid\":\"68000108011\"," +
    //             "\"startavistatus\":\"0\",\"endavistatus\":\"0\",\"traveltime\":\"100\"," +
    //             "\"datacollecttime\":\"2020/04/30 16:01:00\"}],\"offset\":0,\"total\":324,\"limit\":400}}";
    //     ResponseEntity<String> openData = new ResponseEntity<>(openDataStr, HttpStatus.OK);
    //     String queryUrl = travelTimeUrl + "&limit=400&offset=0";
    //     Mockito.when(restTemplate.getForEntity(queryUrl, String.class)).thenReturn(openData);
    //     Mockito.when(openDataService.getTravelTime()).thenReturn(new int[]{100, 50});
    //     int[] travelTime = openDataService.getTravelTime();

    //     Assert.assertEquals(travelTime[0], 100);
    //     Assert.assertEquals(travelTime[1], 50 );
    // }
}