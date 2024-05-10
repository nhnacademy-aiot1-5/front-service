package live.ioteatime.frontservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import live.ioteatime.frontservice.dto.response.ModbusSensorResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelDto {
    @JsonProperty("channel_id")
    private int id;
    @JsonProperty("channel_name")
    private String channelName;
    private int address;
    private String type;
    @JsonProperty("function_code")
    private int functionCode;
    @JsonProperty("sensor_id")
    private int sensorId;
    @JsonProperty("place_id")
    private int placeId;
    @JsonProperty("place_name")
    private String placeName;
    private ModbusSensorResponse sensor;
    private PlaceDto place;
}
