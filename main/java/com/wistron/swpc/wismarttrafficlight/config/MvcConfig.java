package com.wistron.swpc.wismarttrafficlight.config;

import com.wistron.swpc.wismarttrafficlight.interceptor.ApplicationInterceptor;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private ApplicationInterceptor applicationInterceptor;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        try {
            ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient());
            return new RestTemplate(requestFactory);
        } catch (Exception e) {
            return builder.build();
        }
    }

    /**
     * Apache HttpClient
     * 绕过ssl 验证
     */
    private HttpClient httpClient() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(65000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(1000)
                .build();
        SSLContext sslContext = SSLContextBuilder.create().setProtocol(SSLConnectionSocketFactory.SSL).loadTrustMaterial((x, y) -> true).build();
        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setMaxConnTotal(200)
                .setMaxConnPerRoute(20)
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier((x, y) -> true)
                .build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(applicationInterceptor).addPathPatterns("/api/**");
    }
}
