package live.ioteatime.frontservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetModbusSensorChannelResponse {
    private int id;
    @JsonProperty("channel_name")
    private String channelName;
    private GetModbusSensorResponse sensor;
    private PlaceResponse place;
}
