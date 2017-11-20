package com.campaign.monitor.service;

import com.campaign.monitor.model.list.SubscriberList;

import java.util.List;

/**
 * Created by ranjeethpt on 20/11/17.
 *
 * @author ranjeethpt
 */
public interface CampaignMonitorApiService {
    List<SubscriberList> getAllListForClient(String clientId);
}
