package live.ioteatime.frontservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
@NoArgsConstructor
public class ChangePasswordRequest {
    @NotBlank String currentPassword;
    @NotBlank String newPassword;
    @NotBlank String passwordCheck;
}
