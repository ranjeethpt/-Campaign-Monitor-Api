package com.campaign.monitor.service;

import com.campaign.monitor.model.list.SubscriberList;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * Created by ranjeethpt on 20/11/17.
 *
 * @author ranjeethpt
 */
public class CampaignMonitorApiServiceTest {

    private MockRestServiceServer mockServer;

    private String baseUrl = "http://IamGroot.com";

    private String apiKey = "GuardiansOfGalaxy";

    private CampaignMonitorApiService campaignMonitorApiService;

    @Before
    public void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        mockServer = MockRestServiceServer.createServer(restTemplate);

        campaignMonitorApiService = new CampaignMonitorApiServiceImpl(restTemplate, baseUrl, apiKey);
    }

    /**
     * It should call the List’s details API for the client Id passed by setting Authorization header and get all Lists
     *
     * @see <a href="https://www.campaignmonitor.com/api/clients/#getting-subscriber-lists">getting-subscriber-lists</a>
     */
    @Test
    public void testGetAllLists() {
        mockServer.expect(requestTo(baseUrl + "/clients/G_of_Galaxy/lists.json"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("Authorization", "Bearer " + apiKey))
                .andRespond(withSuccess(new ClassPathResource("list-response.json", CampaignMonitorApiServiceTest.class), APPLICATION_JSON));

        List<SubscriberList> subscriberLists = campaignMonitorApiService.getAllListForClient("G_of_Galaxy");

        assertThat(subscriberLists).isNotNull().hasSize(2);
        assertThat(subscriberLists.get(0).getListID()).isEqualTo("Groot");
        assertThat(subscriberLists.get(0).getName()).isEqualTo("I am Groot");
        assertThat(subscriberLists.get(1).getListID()).isEqualTo("RR");
        assertThat(subscriberLists.get(1).getName()).isEqualTo("I am Rocket Racoon");
    }

    /**
     * It should return empty Array when body is empty
     */
    @Test
    public void testGetAllListsEmpty() {
        mockServer.expect(requestTo(baseUrl + "/clients/G_of_Galaxy/lists.json"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("Authorization", "Bearer " + apiKey))
                .andRespond(withSuccess(new ClassPathResource("empty-response.json", CampaignMonitorApiServiceTest.class), APPLICATION_JSON));

        List<SubscriberList> subscriberLists = campaignMonitorApiService.getAllListForClient("G_of_Galaxy");
        assertThat(subscriberLists).isNotNull().isEmpty();
    }

}