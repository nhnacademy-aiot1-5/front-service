package live.ioteatime.frontservice.adaptor;

import live.ioteatime.frontservice.dto.LoginRequest;
import live.ioteatime.frontservice.dto.RegisterRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "gateway-service")
public interface UserAdaptor {

    //회원가입임
    @PostMapping("/api/users")
    ResponseEntity<Void> createUser(@RequestBody RegisterRequest registerRequest);

    //로그인임
    @PostMapping("/auth/login")
    ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest);

}
