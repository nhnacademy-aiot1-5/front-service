package live.ioteatime.frontservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import live.ioteatime.frontservice.domain.Alive;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ModbusSensorResponse {
    @JsonProperty("sensor_id")
    private int id;
    @JsonProperty("sensor_name")
    private String sensorName;
    @JsonProperty("sensor_model_name")
    private String modelName;
    private String ip;
    private String port;
    @JsonProperty("channel_count")
    private int channelCount;
    private Alive alive;
}
