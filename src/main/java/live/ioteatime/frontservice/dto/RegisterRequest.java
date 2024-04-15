package live.ioteatime.frontservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank
    private String id;

    @JsonProperty("pw")
    @NotBlank
    private String password;

    @NotBlank
    private String name;
}
