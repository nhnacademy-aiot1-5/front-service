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

@FeignClient(value = "gateway-service", contextId = "electricity-adaptor")
public interface ElectricityAdaptor {
    @GetMapping("/api/monthly/electricity/last")
    ResponseEntity<MonthlyElectricityDto> getLastMonthElectricity();

    @GetMapping("/api/monthly/electricity/current")
    ResponseEntity<MonthlyElectricityDto> getcurrentMonthElectricity();

    @GetMapping("/api/daily/electricity/last")
    ResponseEntity<MonthlyElectricityDto> getLastDayElectricity();

    @GetMapping("/api/daily/electricity/current")
    ResponseEntity<MonthlyElectricityDto> getCurrentDayElectricity();

    @GetMapping("/api/realtime/electricity")
    ResponseEntity<List<RealtimeElectricityResponseDto>> getRealtimeElectricity(@RequestParam int organizationId);

    @GetMapping("/api/hourly/electricities/total")
    ResponseEntity<List<PreciseElectricitiesDto>> getOneHourTotalElectricties(@RequestParam int organizationId);

    @GetMapping("/api/predicted")
    ResponseEntity<List<PreciseElectricitiesDto>> getMonthlyPredictedValues(
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @RequestParam LocalDateTime requestTime);

    @GetMapping("/api/daily/electricities/total")
    ResponseEntity<List<DailyElectricityDto>> getMontlyTotalElectricities(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                          @RequestParam("localDateTime") LocalDateTime localDateTime,
                                                                          @RequestParam("organizationId") int organizationId);

}
