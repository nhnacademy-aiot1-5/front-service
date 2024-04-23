package live.ioteatime.frontservice.adaptor;

import live.ioteatime.frontservice.dto.response.BudgetHistoryResponse;
import live.ioteatime.frontservice.dto.response.GetUserResponse;
import live.ioteatime.frontservice.dto.response.OrganizationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "gateway-service", contextId = "admin-adaptor")
public interface AdminAdaptor {

    //GUEST 권한을 가진 유저 정보 리스트를 가져옴
    @GetMapping("/api/admin/guests")
    ResponseEntity<List<GetUserResponse>> requestGuestUsers();

    //유저 정보 리스트를 가져옴
    @GetMapping("/api/admin/users")
    ResponseEntity<List<GetUserResponse>> requestUsers();

    //센서 정보 리스트를 가져옴
    @GetMapping("/api/admin/sensors")
    ResponseEntity<List<GetUserResponse>> requestSensors();

    //요금 내역 리스트를 가져옴
    @GetMapping("/api/admin/budgethistory")
    ResponseEntity<List<BudgetHistoryResponse>> requestBudgetHistory();

    //조직의 이름과 조직코드를 가져옴
    @GetMapping("/api/admin/organization")
    ResponseEntity<OrganizationResponse> requestOrganization();

    //조직 코드를 중복체크함
    @GetMapping("/api/admin/checkcode")
    ResponseEntity<Boolean> requestCheckcode(@RequestParam String code);

    //해당하는 유저의 권한을 GUEST에서 USER로 바꿈
    @PutMapping("/api/admin/roles")
    ResponseEntity<String> requestRole(@RequestParam String userId);

    //조직의 목표 요금을 설정함
    @PutMapping("/api/admin/budget")
    ResponseEntity<String> updateBudget(@RequestParam Long budget);

    //조직의 이름을 설정함
    @PutMapping("/api/admin/organizationname")
    ResponseEntity<String> updateOrganizationName(@RequestParam String name);

    //조직의 코드를 설정함
    @PutMapping("/api/admin/organizationcode")
    ResponseEntity<String> updateOrganizationCode(@RequestParam String code);
}
