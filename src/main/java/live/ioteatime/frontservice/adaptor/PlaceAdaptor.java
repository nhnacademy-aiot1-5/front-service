package live.ioteatime.frontservice.adaptor;

import live.ioteatime.frontservice.dto.PlaceDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "gateway-service", contextId = "place-adaptor")
public interface PlaceAdaptor {
    @GetMapping("/api/places")
    ResponseEntity<List<PlaceDto>> getPlaces(@RequestParam int organizationId);

    @PostMapping("/api/place")
    ResponseEntity<String> createPlace(@RequestBody PlaceDto placeDto);

    @PutMapping("/api/place")
    ResponseEntity<String> updatePlace(@RequestParam int placeId, @RequestParam String placeName);

    @DeleteMapping("/api/place")
    ResponseEntity<String> deletePlace(@RequestParam int placeId);
}
