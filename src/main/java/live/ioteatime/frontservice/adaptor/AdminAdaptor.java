package live.ioteatime.frontservice.adaptor;

import live.ioteatime.frontservice.dto.response.GetUserResponse;
import live.ioteatime.frontservice.dto.response.OrganizationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    //조직의 설정 금액을 가져옴
    @GetMapping("/api/admin/budget")
    ResponseEntity<OrganizationResponse> requestBudget();

    //센서 정보 리스트를 가져옴
    @GetMapping("/api/admin/sensors")
    ResponseEntity<List<GetUserResponse>> requestSensors();

    //해당하는 유저의 권한을 GUEST에서 USER로 바꿈
    @PutMapping("/api/admin/roles")
    ResponseEntity<GetUserResponse> requestRole(@RequestParam String userId);

    @PutMapping("/api/admin/budget")
    ResponseEntity<String> updateBudget(@RequestParam Long budget);
}
