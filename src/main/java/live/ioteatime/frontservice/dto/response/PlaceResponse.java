package live.ioteatime.frontservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PlaceResponse {
    @JsonProperty("place_id")
    private int id;
    @JsonProperty("place_name")
    private String placeName;
}
