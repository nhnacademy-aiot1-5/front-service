package live.ioteatime.frontservice.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class TopicRequest {
    private String topic;
    private String description;
}
