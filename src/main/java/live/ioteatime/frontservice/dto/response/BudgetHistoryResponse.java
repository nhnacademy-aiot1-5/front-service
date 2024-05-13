package live.ioteatime.frontservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BudgetHistoryResponse {
    private int id;
    @JsonProperty("change_time")
    private LocalDateTime changeTime;
    private long budget;
}
