package live.ioteatime.frontservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterRequest {
    private String id;
    @JsonProperty("pw")
    private String password;
    private String name;
}
