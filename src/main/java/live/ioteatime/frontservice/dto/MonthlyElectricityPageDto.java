package live.ioteatime.frontservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MonthlyElectricityPageDto {
    private LocalDateTime time;
    private long kwh;
    private List<DailyElectricityDto> dailyElectricityDtos;
    private List<MonthlyElectricityDto> monthlyElectricityDtos;
}
