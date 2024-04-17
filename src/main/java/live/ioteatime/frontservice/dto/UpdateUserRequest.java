package live.ioteatime.frontservice.dto;

import live.ioteatime.frontservice.domain.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class UpdateUserRequest {
    private String id;
    private String name;
    private String createdAt;
    private Role role;
    private String organizationName;
}
