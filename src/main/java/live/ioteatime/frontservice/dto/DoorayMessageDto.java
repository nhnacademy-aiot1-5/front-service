package live.ioteatime.frontservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DoorayMessageDto {
    private String botName;
    private String text;
}
