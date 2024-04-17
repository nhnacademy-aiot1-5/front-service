package live.ioteatime.frontservice.dto.response;

import live.ioteatime.frontservice.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetUserResponse {
    private String id;
    private String name;
    private String createdAt;
    private Role role;
    private String organizationName;
}
