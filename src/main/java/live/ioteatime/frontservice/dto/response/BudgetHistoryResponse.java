package live.ioteatime.frontservice.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BudgetHistoryResponse {
    private int id;
    private LocalDateTime changeTime;
    private long budget;
}
