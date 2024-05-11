package live.ioteatime.frontservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import live.ioteatime.frontservice.domain.Alive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ModbusSensorRequest {
    @JsonProperty("sensor_id")
    int id;
    @JsonProperty("sensor_name")
    String sensorName;
    @JsonProperty("sensor_model_name")
    String modelName;
    String ip;
    String port;
    @JsonProperty("channel_count")
    int channelCount;
    private Alive alive;
}
