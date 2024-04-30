package live.ioteatime.frontservice.dto.response;

import live.ioteatime.frontservice.domain.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetUserResponse {
    private String id;
    private String name;
    private String createdAt;
    private Role role;
    private OrganizationResponse organization;
}
