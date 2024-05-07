package live.ioteatime.frontservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank
    private String id;

    @JsonProperty("pw")
    @NotBlank
    private String password;

    @NotBlank
    private String passwordCheck;

    @NotBlank
    private String name;
    @NotEmpty
    private String organizationName;
    @NotEmpty
    private String organizationCode;
}
