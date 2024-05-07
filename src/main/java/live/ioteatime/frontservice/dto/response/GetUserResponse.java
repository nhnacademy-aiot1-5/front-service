package live.ioteatime.frontservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import live.ioteatime.frontservice.domain.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class GetUserResponse {
    private String id;
    private String name;
    @JsonProperty("created_at")
    private String createdAt;
    private Role role;
    private OrganizationResponse organization;
}
