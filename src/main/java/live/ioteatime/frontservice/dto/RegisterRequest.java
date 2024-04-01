package live.ioteatime.frontservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegisterRequest {
    private String id;
    private String password;
    private String name;
}
