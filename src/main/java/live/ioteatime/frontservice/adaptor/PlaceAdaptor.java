package live.ioteatime.frontservice.adaptor;

import live.ioteatime.frontservice.dto.response.GetPlaceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "gateway-service", contextId = "place-adaptor")
public interface PlaceAdaptor {
    @GetMapping("/api/places")
    ResponseEntity<List<GetPlaceResponse>> getPlaces(@RequestParam int organizationId);
}
