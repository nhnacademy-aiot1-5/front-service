package live.ioteatime.frontservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter @NoArgsConstructor
public class MqttSensorRequest {
    @JsonProperty("sensor_model_name")
    String modelName;
    @JsonProperty("place_id")
    int placeId;
    @JsonProperty("sensor_name")
    String sensorName;
    String ip;
    String port;
    String topic;
    String description;
}
