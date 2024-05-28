package live.ioteatime.frontservice.adaptor;

import live.ioteatime.frontservice.dto.ChannelDto;
import live.ioteatime.frontservice.dto.DailyElectricityDto;
import live.ioteatime.frontservice.dto.MonthlyElectricityDto;
import live.ioteatime.frontservice.dto.PlaceDto;
import live.ioteatime.frontservice.dto.request.ChangePasswordRequest;
import live.ioteatime.frontservice.dto.request.LoginRequest;
import live.ioteatime.frontservice.dto.request.RegisterRequest;
import live.ioteatime.frontservice.dto.request.UpdateUserRequest;
import live.ioteatime.frontservice.dto.response.GetUserResponse;
import live.ioteatime.frontservice.dto.response.LoginResponse;
import live.ioteatime.frontservice.dto.response.OrganizationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * USER 권한이 필요한 기능을 수행하는 Adaptor 클래스 입니다.
 */
@FeignClient(value = "gateway-service", contextId = "user-adaptor")
public interface UserAdaptor {

    /**
     * 유저를 등록합니다. (회원가입)
     * @param registerRequest 등록할 유저의 정보를 request body로 받아옵니다. (id, 비밀번호, 비밀번호 확인 문자, 이름, 조직 이름, 조직 코드)
     * @return 회원가입 결과를 반환합니다.
     */
    @PostMapping("/api/users")
    ResponseEntity<String> createUser(@RequestBody RegisterRequest registerRequest);

    /**
     * 로그인 기능을 수행합니다.
     * @param loginRequest 로그인 요청을 request body로 받아옵니다. (type, token)
     * @return 로그인 결과를 반환합니다.
     */
    @PostMapping("/auth/login")
    ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest);

    /**
     * 유저 정보를 가져옵니다.
     * @return 유저 정보를 리턴합니다. (id, name, 생성일, Role, OrganizationResponse)
     *         Role - (GUEST, USER, ADMIN)
     *         OrganizationResponse - (id, 이름, 설정 요금, 조직 코드)
     */
    @GetMapping("/api/users")
    ResponseEntity<GetUserResponse> getUser();

    /**
     * 조직의 설정 금액을 가져옵니다.
     * @return 조직 정보를 리턴합니다. (id, name, 설정 요금, 조직 코드)
     */
    @GetMapping("/api/users/budget")
    ResponseEntity<OrganizationResponse> requestBudget();

    /**
     * 유저의 역할을 GUEST에서 USER로 업그레이드 합니다.
     * @return 업그레이드 결과를 반환합니다.
     */
    @PostMapping("/api/users/upgrade-request")
    ResponseEntity<String> requestUpgradeToUser();

    /**
     * 조직 정보를 가져옵니다.
     * @return 조직 정보를 반환합니다. (id, name, 설정 요금, 조직 코드)
     */
    @GetMapping("/api/users/organization")
    ResponseEntity<OrganizationResponse> getOrganization();

    /**
     * 유저 정보를 업데이트 합니다.
     * @param updateUserRequest 업데이트 할 유저의 정보를 RequestBody로 받아옵니다. (id, name, 생성일, Role)
     *                          Role - (GUEST, USER, ADMIN)
     * @return 업데이트 결과를 반환합니다.
     */
    @PutMapping("/api/users/update-user")
    ResponseEntity<String> updateUser(@RequestBody UpdateUserRequest updateUserRequest);

    /**
     * 유저의 비밀번호를 변경합니다.
     * @param changePasswordRequest 비밀번호 변경 요청을 RequestBody로 받아옵니다. (현재 비밀번호, 새 비밀번호, 비밀번호 확인 문자)
     * @return 업데이트 결과를 리턴합니다.
     */
    @PutMapping("/api/users/password")
    ResponseEntity<String> updateUserPassword(@RequestBody ChangePasswordRequest changePasswordRequest);

    /**
     * 월 별 사용 전력량을 가져옵니다.
     * @param localDateTime 조회할 날짜를 파라미터로 가져옵니다.
     * @param channelId 조회할 채널의 id 입니다.
     * @return 월 별 전력 사용량에 대한 정보를 리턴합니다. (time, kwh, bill)
     */
    @GetMapping("/api/monthly/electricity")
    ResponseEntity<MonthlyElectricityDto> getMonthlyElectricity(@RequestParam
                                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                LocalDateTime localDateTime,
                                                                @RequestParam int channelId);

    /**
     * 일 별 전력 사용량을 가져옵니다.
     * @param localDateTime 조회할 날짜를 파라미터로 가져옵니다.
     * @param channelId 조회할 채널 id를 파라미터로 가져옵니다.
     * @return 일 별 전력 사용량에 대한 정보를 리턴합니다. (time, kwh, bill)
     */
    @GetMapping("/api/daily/electricities")
    ResponseEntity<List<DailyElectricityDto>> getDailyElectricities(@RequestParam
                                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                    LocalDateTime localDateTime,
                                                                    @RequestParam int channelId);

    /**
     * 월 별 전력 사용 정보 리스트를 가져옵니다.
     * @param localDateTime 조회할 날짜를 파라미터로 가져옵니다.
     * @param channelId 조회할 채널 id를 파라미터로 가져옵니다.
     * @return 월 별 전력 사용량에 대한 정보 리스트를 리턴합니다. (time, kwh, bill)
     */
    @GetMapping("/api/monthly/electricities")
    ResponseEntity<List<MonthlyElectricityDto>> getMonthlyElectricities(@RequestParam
                                                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                        LocalDateTime localDateTime,
                                                                        @RequestParam int channelId);

    /**
     * 조직 id로 장소 리스트를 검색합니다.
     * @param organizationId 검색할 조직의 id를 RequestParam으로 가져옵니다.
     * @return 조직에 속한 장소 리스트를 반환합니다. (id, 장소 이름, 조직 id, OrganizationResponse)
     *         OrganizationResponse - (id, name, 설정 요금, 조직 코드)
     */
    @GetMapping("/api/places")
    ResponseEntity<List<PlaceDto>> getPlacesByOrganizationId(@RequestParam int organizationId);

    /**
     * 장소 id로 해당 장소에 등록된 채널 리스트를 검색합니다.
     * @param placeId 검색할 장소 id를 RequestParam으로 가져옵니다.
     * @return 채널 리스트를 반환합니다.
     *          ChannelDto - (채널 id, 센서 id, 장소 id, 채널 이름, address, quantity, function code, GetModbusSensorResponse, PlaceResponse)
     *                       GetModbusSensorResponse - (id, 이름, 모델명, ip, port, 채널 수, Alive)
     *                                                  Alive - UP, DOWN
     *                       PlaceResponse - (id, 장소 이름)
     */
    @GetMapping("/api/sensors/modbus/channels/by-place")
    ResponseEntity<List<ChannelDto>> getChannelsByPlaceId(@RequestParam int placeId);
}
