package live.ioteatime.frontservice.adaptor;

import live.ioteatime.frontservice.dto.GetUserResponse;
import live.ioteatime.frontservice.dto.LoginRequest;
import live.ioteatime.frontservice.dto.LoginResponse;
import live.ioteatime.frontservice.dto.RegisterRequest;
import live.ioteatime.frontservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "gateway-service")
public interface UserAdaptor {

    //회원가입임
    @PostMapping("/api/users")
    ResponseEntity<Void> createUser(@RequestBody RegisterRequest registerRequest);

    //로그인임
    @PostMapping("/auth/login")
    ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest);

    @GetMapping("/api/users/{userId}")
    ResponseEntity<GetUserResponse> getUsers(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @PathVariable("userId") String userId);


}
