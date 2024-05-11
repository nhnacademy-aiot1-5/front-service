package live.ioteatime.frontservice.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter @Setter
public class PreciseElectricitiesDto {
    private LocalDateTime time;
    private Double kwh;
}