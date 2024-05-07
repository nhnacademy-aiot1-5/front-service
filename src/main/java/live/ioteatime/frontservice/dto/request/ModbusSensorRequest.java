package live.ioteatime.frontservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ModbusSensorRequest {
    @JsonProperty("sensor_model_name")
    String modelName;
    @JsonProperty("sensor_name")
    String sensorName;
    String ip;
    String port;
    @JsonProperty("channel_count")
    int channelCount;
    String description;
}
