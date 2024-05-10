package live.ioteatime.frontservice.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RealtimeElectricityResponseDto {
    private String place;
    private String channel;
    private Double w;
}
