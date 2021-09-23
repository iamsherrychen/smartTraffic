package com.wistron.swpc.wismarttrafficlight.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.wistron.swpc.wismarttrafficlight.service.UtilityService;
import com.wistron.swpc.wismarttrafficlight.vo.UtilityVO;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;

@Service
public class UtilityService {

    public void sendMessage(UtilityVO vo) {

        CloseableHttpClient httpclient = HttpClients.createDefault();        
        HttpPost httpPost = new HttpPost("https://notify-api.line.me/api/notify");
        List<NameValuePair> httpBodyList = new ArrayList<NameValuePair>();
        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
        httpPost.setHeader("Authorization", "Bearer " + vo.getToken());
        httpBodyList.add(new BasicNameValuePair("message", vo.getMessage()));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(httpBodyList, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    
        try {
            httpclient.execute(httpPost);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
