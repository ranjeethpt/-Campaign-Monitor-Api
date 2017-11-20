package com.campaign.monitor;

import com.campaign.monitor.configuration.MainConfiguration;
import com.campaign.monitor.model.list.CMList;
import com.campaign.monitor.model.list.CustomField;
import com.campaign.monitor.model.list.Subscriber;
import com.campaign.monitor.service.CampaignMonitorApiService;
import lombok.Getter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

import static com.campaign.monitor.configuration.MainConfiguration.CAMPAIGN_MONITOR_API_SERVICE_BEAN_NAME;
import static com.campaign.monitor.model.list.UNSUBSCRIBE_SETTINGS.AllClientLists;
import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Created by ranjeethpt on 21/11/17.
 *
 * @author ranjeethpt
 */
public class Client {
    private static final ApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfiguration.class);

    @Getter
    private final CampaignMonitorApiService campaignMonitorApiService;

    public Client() {
        this.campaignMonitorApiService = applicationContext.getBean(CAMPAIGN_MONITOR_API_SERVICE_BEAN_NAME, CampaignMonitorApiService.class);
    }

    public static void main(String[] args) throws URISyntaxException {
        Client client = new Client();
        CampaignMonitorApiService campaignMonitorApiService = client.getCampaignMonitorApiService();
        System.out.println("Api key is configured in configuration-default.properties file");

        System.out.println("Enter client Id");
        Scanner s = new Scanner(System.in);
        String clientId = s.nextLine();
        if (isNotBlank(clientId)) {
            System.out.println("All Lists for Client Id " + clientId + " ####");
            System.out.println(campaignMonitorApiService.getAllListForClient(clientId));
        } else {
            clientId = "groot";
            System.out.println("Client Id cannot be blank. Using default client " + clientId);
        }
        CMList cmList = new CMList();
        cmList.setTitle("Guardians of Galaxy");
        cmList.setUnsubscribeSetting(AllClientLists);
        cmList.setConfirmedOptIn(false);
        cmList.setUnsubscribePage(new URI("http://www.gog.com/unsubscribed.html"));
        cmList.setConfirmationSuccessPage(new URI("http://www.gog.com/joined.html"));

        System.out.println("Creating default list for client Id " + clientId + " ####");
        String listID = campaignMonitorApiService.createList(new CMList(), clientId);
        System.out.println("List Created id is " + listID);

        System.out.println("Subscribing with default data for list Id " + listID + " ####");
        Subscriber subscriber = new Subscriber();
        subscriber.setEmailAddress("groot@gog.com");
        subscriber.setName("Groot");
        subscriber.setCustomFields(newArrayList(new CustomField("key1", "value1"), new CustomField("key2", "value2")));
        subscriber.setResubscribe(true);
        subscriber.setRestartSubscriptionBasedAutoresponders(true);
        String result = campaignMonitorApiService.subscribe(subscriber, listID);
        System.out.println("Subscribed to " + result + "for list id " + listID);
    }
}
