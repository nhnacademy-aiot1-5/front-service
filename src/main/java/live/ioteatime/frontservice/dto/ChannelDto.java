package live.ioteatime.frontservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import live.ioteatime.frontservice.dto.response.GetModbusSensorResponse;
import live.ioteatime.frontservice.dto.response.PlaceResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelDto {
    @JsonProperty("channel_id")
    private int id;
    @JsonProperty("sensor_id")
    private int sensorId;
    @JsonProperty("place_id")
    private int placeId;
    @JsonProperty("channel_name")
    private String channelName;
    private int address;
    private int quantity;
    @JsonProperty("function_code")
    private int functionCode;
    private GetModbusSensorResponse sensor;
    private PlaceResponse place;
}
