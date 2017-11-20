package com.campaign.monitor.model.list;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * Created by ranjeethpt on 21/11/17.
 *
 * @author ranjeethpt
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Subscriber {
    @JsonProperty("EmailAddress")
    private String emailAddress;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("CustomFields")
    private List<CustomField> customFields;
    @JsonProperty("Resubscribe")
    private Boolean resubscribe;
    @JsonProperty("RestartSubscriptionBasedAutoresponders")
    private Boolean restartSubscriptionBasedAutoresponders;
}
