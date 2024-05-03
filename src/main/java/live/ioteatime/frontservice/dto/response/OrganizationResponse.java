package live.ioteatime.frontservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrganizationResponse {
    private int id;
    private String name;
    @JsonProperty("electricity_budget")
    private Long electricityBudget;
    @JsonProperty("organization_code")
    private String organizationCode;
}
