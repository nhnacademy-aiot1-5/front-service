package live.ioteatime.frontservice.adaptor;

import live.ioteatime.frontservice.dto.request.ChangePasswordRequest;
import live.ioteatime.frontservice.dto.request.LoginRequest;
import live.ioteatime.frontservice.dto.request.RegisterRequest;
import live.ioteatime.frontservice.dto.request.UpdateUserRequest;
import live.ioteatime.frontservice.dto.response.GetUserResponse;
import live.ioteatime.frontservice.dto.response.LoginResponse;
import live.ioteatime.frontservice.dto.response.OrganizationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "gateway-service", contextId = "user-adaptor")
public interface UserAdaptor {

    //회원가입임
    @PostMapping("/api/users")
    ResponseEntity<String> createUser(@RequestBody RegisterRequest registerRequest);

    //로그인임
    @PostMapping("/auth/login")
    ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest);

    //유저 정보 가져오는거임
    @GetMapping("/api/users")
    ResponseEntity<GetUserResponse> getUser();

    //조직의 설정 금액을 가져옴
    @GetMapping("/api/users/budget")
    ResponseEntity<OrganizationResponse> requestBudget();

    @PostMapping("/api/users/upgrade-request")
    ResponseEntity<String> requestUpgradeToUser();

    @GetMapping("/api/users/organization")
    ResponseEntity<OrganizationResponse> getOrganization();

    @PutMapping("/api/users/update-user")
    ResponseEntity<String> updateUser(@RequestBody UpdateUserRequest updateUserRequest);

    @PutMapping("/api/users/password")
    ResponseEntity<String> updateUserPassword(@RequestBody ChangePasswordRequest changePasswordRequest);

}
