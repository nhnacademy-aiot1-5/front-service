package live.ioteatime.frontservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class GetTopicResponse {
    @JsonProperty("topic_id")
    private int id;
    private String topic;
    private String description;
}
