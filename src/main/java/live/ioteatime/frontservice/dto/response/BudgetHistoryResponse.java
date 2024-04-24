package live.ioteatime.frontservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BudgetHistoryResponse {
    private int id;
    private LocalDateTime changeTime;
    private long budget;
}
