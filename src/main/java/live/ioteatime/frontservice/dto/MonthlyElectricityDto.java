package live.ioteatime.frontservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyElectricityDto {
    private LocalDateTime time;
    private long kwh;
    private long bill;
}
