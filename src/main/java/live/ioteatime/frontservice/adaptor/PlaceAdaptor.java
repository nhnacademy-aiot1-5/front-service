package live.ioteatime.frontservice.adaptor;

import live.ioteatime.frontservice.dto.PlaceDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 조직에 등록된 장소 리스트를 가져오고, 등록/수정/삭제에 대한 기능을 수행하는 Adaptor 클래스 입니다.
 */
@FeignClient(value = "gateway-service", contextId = "place-adaptor")
public interface PlaceAdaptor {
    /**
     * 조직에 등록된 장소 리스트를 가져옵니다.
     * @param organizationId 장소를 검색할 조직의 id입니다.
     * @return 장소 리스트를 반환합니다. (장소 id, 장소 이름, 조직 id, OrganizationResponse)
     *         OrganizationResponse - (id, name, 설정 요금, 조직 코드)
     */
    @GetMapping("/api/places")
    ResponseEntity<List<PlaceDto>> getPlacesByOrganizationId(@RequestParam int organizationId);

    /**
     * 장소를 등록합니다.
     * @param placeDto 등록할 장소에 대한 정보를 파라미터로 받아옵니다. (id, 장소명, 조직 id, OrganizationResponse)
     *                 OrganizationResponse - (id, name, 설정 요금, 조직 코드)
     * @return 등록 결과를 리턴합니다.
     */
    @PostMapping("/api/place")
    ResponseEntity<String> createPlace(@RequestBody PlaceDto placeDto);

    /**
     * 장소를 업데이트합니다.
     * @param placeId 업데이트 할 장소의 id 입니다.
     * @param placeName 바꾸고싶은 이름을 파라미터로 받아옵니다.
     * @return 업데이트 결과를 리턴합니다.
     */
    @PutMapping("/api/place")
    ResponseEntity<String> updatePlace(@RequestParam int placeId, @RequestParam String placeName);

    /**
     * 장소를 삭제합니다.
     * @param placeId 삭제하고싶은 장소의 id입니다.
     * @return 삭제 결과를 반환합니다.
     */
    @DeleteMapping("/api/place")
    ResponseEntity<String> deletePlace(@RequestParam int placeId);
}
