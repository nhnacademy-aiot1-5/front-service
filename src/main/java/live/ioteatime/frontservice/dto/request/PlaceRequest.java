package live.ioteatime.frontservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PlaceRequest {
    @JsonProperty("place_id")
    private int id;
    @JsonProperty("place_name")
    private String placeName;
    @JsonProperty("organization_id")
    private int organizationId;
}
