package live.ioteatime.frontservice.adaptor;

import live.ioteatime.frontservice.dto.request.PlaceRequest;
import live.ioteatime.frontservice.dto.response.GetPlaceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "gateway-service", contextId = "place-adaptor")
public interface PlaceAdaptor {
    @GetMapping("/api/places")
    ResponseEntity<List<GetPlaceResponse>> getPlaces(@RequestParam int organizationId);

    @PostMapping("/api/place")
    ResponseEntity<String> createPlace(@RequestBody PlaceRequest placeRequest);

    @PutMapping("/api/place/update")
    ResponseEntity<String> updatePlace(@RequestParam int placeId, @RequestParam String placeName);

    @DeleteMapping("/api/place/delete")
    ResponseEntity<String> deletePlace(@RequestParam int placeId);
}
