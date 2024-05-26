package live.ioteatime.frontservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class OutlierRequest {
    @JsonProperty("outlier_id")
    private int outlierId;
}
