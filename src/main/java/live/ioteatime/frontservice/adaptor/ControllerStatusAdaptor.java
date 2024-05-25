package live.ioteatime.frontservice.adaptor;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "gateway-service", contextId = "controller-status-adaptor")
public interface ControllerStatusAdaptor {

    @GetMapping("/api/controller/status")
    ResponseEntity<Integer> getStatus(@RequestParam String controllerId);

    @PutMapping("/api/controller/disable")
    ResponseEntity<Integer> disableController(@RequestParam String controllerId);
}
