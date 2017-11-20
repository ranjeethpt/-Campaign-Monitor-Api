package com.campaign.monitor.model.list;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by ranjeethpt on 21/11/17.
 *
 * @author ranjeethpt
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomField {
    @JsonProperty("Key")
    private String key;
    @JsonProperty("Value")
    private String value;
}
