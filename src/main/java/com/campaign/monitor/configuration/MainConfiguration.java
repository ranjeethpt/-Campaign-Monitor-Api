package com.campaign.monitor.configuration;

import com.campaign.monitor.service.CampaignMonitorApiService;
import com.campaign.monitor.service.CampaignMonitorApiServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Created by ranjeethpt on 21/11/17.
 *
 * @author ranjeethpt
 */
@Configuration
@PropertySource("classpath:configuration-default.properties")
public class MainConfiguration implements EnvironmentAware {

    private Environment env;

    public static final String CAMPAIGN_MONITOR_API_SERVICE_BEAN_NAME = "campaignMonitorApiService";

    @Bean(name = CAMPAIGN_MONITOR_API_SERVICE_BEAN_NAME)
    public CampaignMonitorApiService campaignMonitorApiService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        return new CampaignMonitorApiServiceImpl(restTemplate, env.getProperty("campaign.monitor.baseurlApi"), env.getProperty("campaign.monitor.apiKey"), objectMapper);
    }

    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(env.getProperty("server.timeout.read", Integer.class));
        factory.setConnectTimeout(env.getProperty("server.timeout.connect", Integer.class));
        return new RestTemplate(factory);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }
}
