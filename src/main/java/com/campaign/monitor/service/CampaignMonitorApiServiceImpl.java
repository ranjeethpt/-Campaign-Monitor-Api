package com.campaign.monitor.service;

import com.campaign.monitor.model.list.SubscriberList;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by ranjeethpt on 20/11/17.
 *
 * @author ranjeethpt
 */
public class CampaignMonitorApiServiceImpl implements CampaignMonitorApiService {
    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String apiKey;

    public CampaignMonitorApiServiceImpl(RestTemplate restTemplate, String baseUrl, String apiKey) {
        checkNotNull(restTemplate);
        checkNotBlank(baseUrl);
        checkNotBlank(apiKey);

        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }

    private void checkNotBlank(String string) {
        checkArgument(!isBlank(string));
    }

    @Override
    public List<SubscriberList> getAllListForClient(String clientId) {
        checkNotBlank(clientId);

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.set("Authorization", "Bearer " + apiKey);
        HttpEntity<?> httpEntity = new HttpEntity<>(requestHeaders);
        ResponseEntity<SubscriberList[]> clientResponse = restTemplate.exchange(baseUrl + "/clients/" + clientId + "/lists.json", HttpMethod.GET, httpEntity, SubscriberList[].class);
        if (clientResponse.getStatusCode() != HttpStatus.OK ||
                clientResponse.getBody() == null ||
                clientResponse.getBody().length == 0) {
            return newArrayList();
        } else {
            return Arrays.asList(clientResponse.getBody());
        }
    }
}
