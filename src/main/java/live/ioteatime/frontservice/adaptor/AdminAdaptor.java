package live.ioteatime.frontservice.adaptor;

import live.ioteatime.frontservice.dto.response.BudgetHistoryResponse;
import live.ioteatime.frontservice.dto.response.GetUserResponse;
import live.ioteatime.frontservice.dto.response.OrganizationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Admin 권한이 필요한 기능을 수행하는 Adaptor가 정의된 클래스입니다.
 */
@FeignClient(value = "gateway-service", contextId = "admin-adaptor")
public interface AdminAdaptor {

    /**
     * GUEST 권한을 가진 유저 정보 리스트를 가져옵니다.
     * @return (id, 이름, 생성일, Role, OrganizationResponse) 리스트를 반환합니다.
     *          Role - (GUEST, USER, ADMIN)
     *          OrganizationResponse - (id, name, 설정 요금, 조직 코드)
     */
    @GetMapping("/api/admin/guests")
    ResponseEntity<List<GetUserResponse>> requestGuestUsers();

    /**
     * 유저 정보 리스트를 가져옵니다.
     * @return (id, 이름, 생성일, Role, OrganizationResponse) 리스트를 반환합니다.
     *          Role - (GUEST, USER, ADMIN)
     *          OrganizationResponse - (id, name, 설정 요금, 조직 코드)
     */
    @GetMapping("/api/admin/users")
    ResponseEntity<List<GetUserResponse>> requestUsers();

    /**
     * 설정 요금 변경 내역을 조회합니다.
     * @return (id, 변경 시간, 설정 요금) 리스트를 반환합니다.
     */
    @GetMapping("/api/admin/budget-histories")
    ResponseEntity<List<BudgetHistoryResponse>> requestBudgetHistory();

    /**
     * 조직의 이름과 조직코드를 가져옵니다.
     * @return (id, 조직 이름, 설정 요금, 조직 코드)를 반환합니다.
     */
    @GetMapping("/api/admin/organization")
    ResponseEntity<OrganizationResponse> requestOrganization();

    /**
     * 조직 코드를 중복체크 합니다.
     * @param code 중복 체크를 하고 싶은 조직 코드 입니다.
     * @return 조직코드의 중복 여부를 Boolean 타입으로 리턴합니다.
     */
    @GetMapping("/api/admin/check-code")
    ResponseEntity<Boolean> requestCheckcode(@RequestParam String code);

    /**
     * 해당하는 유저의 권한을 GUEST에서 USER로 바꿉니다.
     * @param userId 권한을 바꿀 유저의 id 입니다.
     * @return 역할 변경 결과를 리턴합니다.
     */
    @PutMapping("/api/admin/role")
    ResponseEntity<String> requestRole(@RequestParam String userId);

    /**
     * 조직의 목표 요금을 설정합니다.
     * @param budget 바꿀 설정 요금입니다.
     * @return 요금 변경 결과를 리턴합니다.
     */
    @PutMapping("/api/admin/budget")
    ResponseEntity<String> updateBudget(@RequestParam Long budget);

    /**
     * 조직의 이름을 설정합니다.
     * @param name 변경할 조직의 이름 입니다.
     * @return 이름 변경 결과를 리턴합니다.
     */
    @PutMapping("/api/admin/organization-name")
    ResponseEntity<String> updateOrganizationName(@RequestParam String name);

    /**
     * 조직의 코드를 설정합니다.
     * @param code 업데이트할 조직 코드 값 입니다.
     * @return 업데이트 결과를 리턴합니다.
     */
    @PutMapping("/api/admin/organization-code")
    ResponseEntity<String> updateOrganizationCode(@RequestParam String code);
}
