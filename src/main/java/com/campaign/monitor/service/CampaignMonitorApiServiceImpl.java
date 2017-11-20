package com.campaign.monitor.service;

import com.campaign.monitor.model.list.CMList;
import com.campaign.monitor.model.list.Subscriber;
import com.campaign.monitor.model.list.SubscriberList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;

    public CampaignMonitorApiServiceImpl(RestTemplate restTemplate, String baseUrl, String apiKey, ObjectMapper objectMapper) {
        checkNotNull(restTemplate);
        checkNotBlank(baseUrl);
        checkNotBlank(apiKey);
        checkNotNull(objectMapper);

        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.objectMapper = objectMapper;
    }

    private void checkNotBlank(String string) {
        checkArgument(!isBlank(string));
    }

    @Override
    public List<SubscriberList> getAllListForClient(String clientId) {
        checkNotBlank(clientId);

        ResponseEntity<SubscriberList[]> clientResponse = restTemplate.exchange(baseUrl + "/clients/" + clientId + "/lists.json",
                HttpMethod.GET, new HttpEntity<>(getAuthEntity()), SubscriberList[].class);
        if (clientResponse.getStatusCode() != HttpStatus.OK ||
                clientResponse.getBody() == null ||
                clientResponse.getBody().length == 0) {
            return newArrayList();
        } else {
            return Arrays.asList(clientResponse.getBody());
        }
    }

    @Override
    public String createList(CMList list, String clientId) {
        try {
            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(list), getAuthEntity());
            return restTemplate.postForObject(baseUrl + "/lists/" + clientId + ".json", entity, String.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("list cannot be converted to json, input list was " + list, e);
        }
    }

    @Override
    public String subscribe(Subscriber subscriber, String listId) {
        try {
            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(subscriber), getAuthEntity());
            return restTemplate.postForObject(baseUrl + "/subscribers/" + listId + ".json", entity, String.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Subscriber cannot be converted to json, input Subscriber was " + subscriber, e);
        }
    }

    private HttpHeaders getAuthEntity() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.set("Authorization", "Bearer " + apiKey);
        return requestHeaders;
    }
}
