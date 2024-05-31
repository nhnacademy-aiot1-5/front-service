package live.ioteatime.frontservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutlierDto {
    int id;
    int organizationId;
    String place;
    String type;
    long time;
    double outlierValue;
    int flag;
}
