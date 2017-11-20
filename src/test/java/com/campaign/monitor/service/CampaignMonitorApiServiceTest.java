package com.campaign.monitor.service;

import com.campaign.monitor.model.list.CMList;
import com.campaign.monitor.model.list.CustomField;
import com.campaign.monitor.model.list.Subscriber;
import com.campaign.monitor.model.list.SubscriberList;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static com.campaign.monitor.model.list.UNSUBSCRIBE_SETTINGS.AllClientLists;
import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
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

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        mockServer = MockRestServiceServer.createServer(restTemplate);
        objectMapper = new ObjectMapper();

        campaignMonitorApiService = new CampaignMonitorApiServiceImpl(restTemplate, baseUrl, apiKey, objectMapper);
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

    /**
     * It should create a list based on the data passed.
     *
     * @see <a href="https://www.campaignmonitor.com/api/lists/>create-lists</a>
     */
    @Test
    public void testCreateList() throws URISyntaxException {
        mockServer.expect(requestTo(baseUrl + "/lists/G_of_Galaxy.json"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Authorization", "Bearer " + apiKey))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.Title", is("Guardians of Galaxy")))
                .andExpect(jsonPath("$.UnsubscribePage", is("http://www.gog.com/unsubscribed.html")))
                .andExpect(jsonPath("$.UnsubscribeSetting", is(AllClientLists.toString())))
                .andExpect(jsonPath("$.ConfirmedOptIn", is(false)))
                .andExpect(jsonPath("$.ConfirmationSuccessPage", is("http://www.gog.com/joined.html")))
                .andRespond(withSuccess("I am Groot", APPLICATION_JSON));

        CMList cmList = new CMList();
        cmList.setTitle("Guardians of Galaxy");
        cmList.setUnsubscribeSetting(AllClientLists);
        cmList.setConfirmedOptIn(false);
        cmList.setUnsubscribePage(new URI("http://www.gog.com/unsubscribed.html"));
        cmList.setConfirmationSuccessPage(new URI("http://www.gog.com/joined.html"));
        assertThat(campaignMonitorApiService.createList(cmList, "G_of_Galaxy")).isNotNull().isEqualTo("I am Groot");
    }


    /**
     * It should create a list based on the data passed.
     *
     * @see <a href="https://www.campaignmonitor.com/api/subscribers/#adding-a-subscriber>adding-a-subscriber</a>
     */
    @Test
    public void testSubscribe() throws URISyntaxException {
        mockServer.expect(requestTo(baseUrl + "/subscribers/G_of_Galaxy.json"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Authorization", "Bearer " + apiKey))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.EmailAddress", is("groot@gog.com")))
                .andExpect(jsonPath("$.Name", is("Groot")))
                .andExpect(jsonPath("$.CustomFields[0].Key", is("key1")))
                .andExpect(jsonPath("$.CustomFields[0].Value", is("value1")))
                .andExpect(jsonPath("$.CustomFields[1].Key", is("key2")))
                .andExpect(jsonPath("$.CustomFields[1].Value", is("value2")))
                .andExpect(jsonPath("$.Resubscribe", is(true)))
                .andExpect(jsonPath("$.RestartSubscriptionBasedAutoresponders", is(true)))
                .andRespond(withSuccess("groot", APPLICATION_JSON));

        Subscriber subscriber = new Subscriber();
        subscriber.setEmailAddress("groot@gog.com");
        subscriber.setName("Groot");
        subscriber.setCustomFields(newArrayList(new CustomField("key1", "value1"), new CustomField("key2", "value2")));
        subscriber.setResubscribe(true);
        subscriber.setRestartSubscriptionBasedAutoresponders(true);

        assertThat(campaignMonitorApiService.subscribe(subscriber, "G_of_Galaxy")).isNotNull().isEqualTo("groot");
    }
}