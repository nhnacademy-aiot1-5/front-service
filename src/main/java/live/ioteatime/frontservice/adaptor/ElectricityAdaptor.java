package live.ioteatime.frontservice.adaptor;

import live.ioteatime.frontservice.dto.DailyElectricityDto;
import live.ioteatime.frontservice.dto.MonthlyElectricityDto;
import live.ioteatime.frontservice.dto.RealtimeElectricityResponseDto;
import live.ioteatime.frontservice.dto.response.PreciseElectricitiesDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 전력 사용량과 요금에 관한 기능을 수행하는 Adaptor 클래스 입니다.
 */
@FeignClient(value = "gateway-service", contextId = "electricity-adaptor")
public interface ElectricityAdaptor {
    /**
     * 지난달 사용한 전력량과 요금을 가져옵니다.
     * @return (time, kwh, bill)을 리턴합니다.
     */
    @GetMapping("/api/monthly/electricity/last")
    ResponseEntity<MonthlyElectricityDto> getLastMonthElectricity();

    /**
     * 이번달 전력 사용량과 요금을 가져옵니다.
     * @return (time, kwh, bill)을 리턴합니다.
     */
    @GetMapping("/api/monthly/electricity/current")
    ResponseEntity<MonthlyElectricityDto> getcurrentMonthElectricity();

    /**
     * 어제 사용한 전력량과 요금을 가져옵니다.
     * @return (time, kwh, bill)을 리턴합니다.
     */
    @GetMapping("/api/daily/electricity/last")
    ResponseEntity<DailyElectricityDto> getLastDayElectricity();

    /**
     * 오늘 사용한 전력량과 요금을 가져옵니다.
     * @return (time, kwh, bill)을 리턴합니다.
     */
    @GetMapping("/api/daily/electricity/current")
    ResponseEntity<DailyElectricityDto> getCurrentDayElectricity();

    /**
     * 채널별 실시간 사용 전력량 리스트를 가져옵니다.
     * @param organizationId
     * @return (장소명, 채널명, w) 리스트를 리턴합니다.
     */
    @GetMapping("/api/realtime/electricity")
    ResponseEntity<List<RealtimeElectricityResponseDto>> getRealtimeElectricity(@RequestParam int organizationId);

    /**
     * 해당 조직이 1시간 동안 사용한 총 전력량 리스트를 가져옵니다.
     * @param organizationId 검색할 조직의 id를 RequestParam으로 가져옵니다.
     * @return (time, kwh) 리스트를 리턴합니다.
     */
    @GetMapping("/api/hourly/electricities/total")
    ResponseEntity<List<PreciseElectricitiesDto>> getOneHourTotalElectricties(@RequestParam int organizationId);

    /**
     * 한달 예측값을 가져옵니다.
     * @param requestTime 검색할 시간을 RequestParam으로 받아옵니다.
     * @return (time, kwh) 리스트를 리턴합니다.
     */
    @GetMapping("/api/predicted")
    ResponseEntity<List<PreciseElectricitiesDto>> getMonthlyPredictedValues(
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @RequestParam LocalDateTime requestTime,
            @RequestParam int organizationId);

    /**
     * 해당 조직이 한달 동안 사용한 총 전력량 리스트를 가져옵니다.
     * @param localDateTime 검색할 시간을 RequestParam으로 가져옵니다.
     * @param organizationId 검색할 조직의 id를 RequestParam으로 가져옵니다.
     * @return (time, kwh, bill) 리스트를 반환합니다.
     */
    @GetMapping("/api/daily/electricities/total")
    ResponseEntity<List<DailyElectricityDto>> getMontlyTotalElectricities(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                          @RequestParam("localDateTime") LocalDateTime localDateTime,
                                                                          @RequestParam("organizationId") int organizationId);

}
