package live.ioteatime.frontservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class LoginResponse {
    String type;
    String token;
}
