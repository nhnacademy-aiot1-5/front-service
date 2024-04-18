package live.ioteatime.frontservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyElectricityDto {
    private Long id;
    private Long kwh;
    private LocalDate time;
}
