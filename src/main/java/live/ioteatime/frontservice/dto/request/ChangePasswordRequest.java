package live.ioteatime.frontservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
@NoArgsConstructor
public class ChangePasswordRequest {

    @NotBlank
    @JsonProperty("current_password")
    String currentPassword;
    @NotBlank
    @JsonProperty("new_password")
    String newPassword;
    @NotBlank
    @JsonProperty("password_check")
    String passwordCheck;
}
