package live.ioteatime.frontservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import live.ioteatime.frontservice.domain.Alive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Place place;

    @Getter @NoArgsConstructor @Setter
    public static class Place {
        private int id;
        @JsonProperty("place_name")
        private String placeName;
    }
}
