package live.ioteatime.frontservice.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrganizationResponse {
    private int id;
    private String name;
    private Long electricityBudget;
    private String organizationCode;
}
