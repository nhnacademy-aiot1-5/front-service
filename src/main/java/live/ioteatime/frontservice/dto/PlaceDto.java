package live.ioteatime.frontservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceDto {
    @JsonProperty("place_id")
    private int id;
    @JsonProperty("place_name")
    private String placeName;
}
