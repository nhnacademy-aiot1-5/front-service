package live.ioteatime.frontservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import live.ioteatime.frontservice.domain.Alive;
import live.ioteatime.frontservice.dto.PlaceDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GetMqttSensorResponse {
    private int id;
    @JsonProperty("sensor_name")
    private String name;
    @JsonProperty("sensor_model_name")
    private String modelName;
    private String ip;
    private String port;
    private Alive alive;
    private PlaceDto place;
}
