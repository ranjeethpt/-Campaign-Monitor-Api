package com.campaign.monitor.model.list;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.net.URI;

/**
 * Created by ranjeethpt on 21/11/17.
 *
 * @author ranjeethpt
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CMList {
    @JsonProperty("Title")
    String title;
    @JsonProperty("UnsubscribePage")
    private URI unsubscribePage;
    @JsonProperty("UnsubscribeSetting")
    private UNSUBSCRIBE_SETTINGS unsubscribeSetting;
    @JsonProperty("ConfirmedOptIn")
    private boolean confirmedOptIn;
    @JsonProperty("ConfirmationSuccessPage")
    private URI confirmationSuccessPage;
}


