package live.ioteatime.frontservice.controller;

import live.ioteatime.frontservice.adaptor.AdminAdaptor;
import live.ioteatime.frontservice.adaptor.ElectricityAdaptor;
import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.domain.Role;
import live.ioteatime.frontservice.dto.*;
import live.ioteatime.frontservice.dto.response.GetUserResponse;
import live.ioteatime.frontservice.dto.response.OrganizationResponse;
import live.ioteatime.frontservice.dto.response.PreciseElectricitiesDto;
import live.ioteatime.frontservice.service.ElectricityService;
import live.ioteatime.frontservice.service.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class IndexController {
    private final UserAdaptor userAdaptor;
    private final AdminAdaptor adminAdaptor;
    private final ElectricityAdaptor electricityAdaptor;
    private final ElectricityService electricityService;
    private final SseService sseService;

    @GetMapping
    public String index(Model model) {
        GetUserResponse userInfo = userAdaptor.getUser().getBody();
        if (userInfo == null) {
            return "redirect:/login";
        }
        model.addAttribute("userInfo", userInfo);

        if (userInfo.getRole() == Role.GUEST) {
            return "redirect:/mypage";
        }

        Double lastMonthKwh = Optional.ofNullable(electricityAdaptor.getLastMonthElectricity().getBody())
                .map(MonthlyElectricityDto::getKwh)
                .orElse(0.0);

        Double todayKwh = Optional.ofNullable(electricityAdaptor.getCurrentDayElectricity().getBody())
                .map(DailyElectricityDto::getKwh)
                .orElse(0.0);

        Double yesterdayKwh = Optional.ofNullable(electricityAdaptor.getLastDayElectricity().getBody())
                .map(DailyElectricityDto::getKwh)
                .orElse(0.0);

        Double thisMonthKwh = Optional.ofNullable(electricityAdaptor.getcurrentMonthElectricity().getBody())
                .map(MonthlyElectricityDto::getKwh)
                .orElse(0.0);

        OrganizationResponse budget = userAdaptor.requestBudget().getBody();
        model.addAttribute("budget", budget);
        model.addAttribute("lastMonthKwh", lastMonthKwh);
        model.addAttribute("todayKwh", todayKwh);
        model.addAttribute("yesterdayKwh", yesterdayKwh);
        model.addAttribute("thisMonthKwh", thisMonthKwh);
        model.addAttribute("wTop10", electricityService.getTop10Electricity());
        model.addAttribute("outliers", sseService.getOutliers(userInfo.getOrganization().getId()));
        return "index";
    }

    @PutMapping("/budget")
    public String updateBudget(@RequestParam Long budget) {
        adminAdaptor.updateBudget(budget);
        return "redirect:/";
    }

    @GetMapping("/top10")
    @ResponseBody
    public List<RealtimeElectricityResponseDto> getTop10() {
        log.debug("프론트 getTop10 인덱스 컨트롤러 위치한 컨트롤러 실행됨");
        return electricityService.getTop10Electricity();
    }

    @GetMapping("/total")
    @ResponseBody
    public List<PreciseElectricitiesDto> getOneHourTotalElectricities() {
        int organizationId = userAdaptor.getUser().getBody().getOrganization().getId();
        return electricityAdaptor.getOneHourTotalElectricties(organizationId).getBody();
    }

    /**
     * 이번 달 1일부터 요청일까지의 일별 전력 소비량 리스트를 반환합니다.
     *
     * @return 일별 전력 소비량 리스트
     */
    @GetMapping("/daily/electricity/current-month")
    @ResponseBody
    public List<DailyElectricityDto> getCurrentMonthDailyTotalElectricities() {
        int organizationId = userAdaptor.getUser().getBody().getOrganization().getId();
        return electricityAdaptor.getMontlyTotalElectricities(LocalDateTime.now(), organizationId).getBody();
    }

    /**
     * 요청일부터 이번 달 마지막 날까지의 일별 전력 소비 예측량 리스트를 반환합니다.
     *
     * @return 일별 전력 소비 예측량 리스트
     */
    @GetMapping("/monthly-predict")
    @ResponseBody
    public List<PreciseElectricitiesDto> getMontlyPredictedElectricities() {
        int organizationId = userAdaptor.getUser().getBody().getOrganization().getId();
        return electricityAdaptor.getMonthlyPredictedValues(organizationId, LocalDateTime.now()).getBody();
    }

    @GetMapping("/kwh")
    @ResponseBody
    public KwhDto getKwh() {
        double lastMonthKwh = electricityAdaptor.getLastMonthElectricity().getBody().getKwh();
        double thisMonthKwh = electricityAdaptor.getcurrentMonthElectricity().getBody().getKwh();
        double yesterdayKwh = electricityAdaptor.getLastDayElectricity().getBody().getKwh();
        double todayKwh = electricityAdaptor.getCurrentDayElectricity().getBody().getKwh();

        return new KwhDto((long) lastMonthKwh, (long) thisMonthKwh, (long) todayKwh, (long) yesterdayKwh);
    }

    @GetMapping("/bill")
    @ResponseBody
    public List<DailyElectricityDto> getCumulativeBills() {
        return electricityService.getCumulativeBills();
    }

    @GetMapping("/bill-predict")
    @ResponseBody
    public List<PreciseElectricitiesDto> getCumulativeBillPredictions() {
        return electricityService.getCumulativeBillPredictions();
    }

    @PostMapping("/send-dooray")
    public ResponseEntity<?> sendDoorayMessage(@RequestBody OutlierDto outlierDto) {
        sseService.sendDoorayMessage("이상치 알림", outlierDto.getId(), outlierDto.getPlace(), outlierDto.getType());
        return ResponseEntity.ok().build();
    }
}
