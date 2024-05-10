package live.ioteatime.frontservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KwhDto {
    long lastMonthKwh;
    long thisMonthKwh;
    long todayKwh;
    long yesterdayKwh;
}
