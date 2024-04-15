package live.ioteatime.frontservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class ChangePasswordRequest {
    @NotBlank String currentPassword;
    @NotBlank String newPassword;
    @NotBlank String passwordCheck;
}
