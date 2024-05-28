package live.ioteatime.frontservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Alert {
    @JsonProperty("deveui")
    private String devEui;
    private String sensorName;
    private String place;
    private String type;
    private int time;
    @JsonProperty("outlier_value")
    private long value;
    private int organizationId;
}