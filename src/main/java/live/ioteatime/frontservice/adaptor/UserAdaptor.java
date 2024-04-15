package live.ioteatime.frontservice.adaptor;

import live.ioteatime.frontservice.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "gateway-service")
public interface UserAdaptor {

    //회원가입임
    @PostMapping("/api/users")
    ResponseEntity<String> createUser(@RequestBody RegisterRequest registerRequest);

    //로그인임
    @PostMapping("/auth/login")
    ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest);

    @GetMapping("/api/users")
    ResponseEntity<GetUserResponse> getUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader);

    @PostMapping("/api/users/upgrade-request")
    ResponseEntity<String> requestUpgradeToUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader);

    @GetMapping("/api/users/organization")
    ResponseEntity<OrganizationResponse> getOrganization(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader);

    @PutMapping("/api/users")
    ResponseEntity<String> updateUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, UpdateUserRequest updateUserRequest);
}
