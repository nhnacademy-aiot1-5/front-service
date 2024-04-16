package live.ioteatime.frontservice.adaptor;

import live.ioteatime.frontservice.dto.GetUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "gateway-service", contextId = "admin-adaptor")
public interface AdminAdaptor {

    //GUEST 권한을 가진 유저 정보 리스트를 가져옴
    @GetMapping("/api/admin/guests")
    ResponseEntity<List<GetUserResponse>> requestGuestUsers(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestParam("page") int page);

    //유저 정보 리스트를 가져옴
    @GetMapping("/api/admin/users")
    ResponseEntity<List<GetUserResponse>> requestUsers(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader);

    //센서 정보 리스트를 가져옴
    @GetMapping("/api/admin/sensors")
    ResponseEntity<List<GetUserResponse>> requestSensors(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader);
}
