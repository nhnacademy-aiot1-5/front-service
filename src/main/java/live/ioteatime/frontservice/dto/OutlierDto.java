package live.ioteatime.frontservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutlierDto {
    @JsonProperty("outlier_id")
    int id;
    String place;
    String type;
    @JsonProperty("outlier_value")
    long outlierValue;
    int flag;
}
