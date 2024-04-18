package live.ioteatime.frontservice.adaptor;

import live.ioteatime.frontservice.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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

    @PostMapping("/api/users/upgrade-request")
    ResponseEntity<String> requestUpgradeToUser();

    @GetMapping("/api/users/organization")
    ResponseEntity<OrganizationResponse> getOrganization();

    @PutMapping("/api/users")
    ResponseEntity<String> updateUser(@RequestBody UpdateUserRequest updateUserRequest);

    @PutMapping("/api/users/password")
    ResponseEntity<String> updateUserPassword(@RequestBody ChangePasswordRequest changePasswordRequest);

    @GetMapping("/api/monthly/electricity")
    ResponseEntity<String> getMonthlyElectricity(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate);

    @GetMapping("/api/daily/electricities")
    ResponseEntity<List<DailyElectricityDto>> getDailyElectricities(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate);

}
