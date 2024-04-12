package live.ioteatime.frontservice.dto;

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
}
