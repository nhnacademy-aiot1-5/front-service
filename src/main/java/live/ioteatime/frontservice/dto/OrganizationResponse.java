package live.ioteatime.frontservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Getter
@NoArgsConstructor
public class OrganizationResponse {
    private int id;
    private String name;
    private Long electricityBudget;
}
