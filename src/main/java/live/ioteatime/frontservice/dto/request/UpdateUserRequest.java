package live.ioteatime.frontservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import live.ioteatime.frontservice.domain.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class UpdateUserRequest {
    private String id;
    private String name;
    @JsonProperty("created_at")
    private String createdAt;
    private Role role;
}
