package live.ioteatime.frontservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MonthlyElectricityDto {
    private long id;
    private long kwh;
    private LocalDate time;
    private List<DailyElectricityDto> dailyElectricityDtos;
}
