package live.ioteatime.frontservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Alert {
    private int organizationId;
    private String message;
}
