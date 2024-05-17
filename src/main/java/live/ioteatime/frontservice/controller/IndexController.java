package live.ioteatime.frontservice.controller;

import live.ioteatime.frontservice.adaptor.AdminAdaptor;
import live.ioteatime.frontservice.adaptor.ElectricityAdaptor;
import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.domain.Role;
import live.ioteatime.frontservice.dto.DailyElectricityDto;
import live.ioteatime.frontservice.dto.KwhDto;
import live.ioteatime.frontservice.dto.RealtimeElectricityResponseDto;
import live.ioteatime.frontservice.dto.response.GetUserResponse;
import live.ioteatime.frontservice.dto.response.OrganizationResponse;
import live.ioteatime.frontservice.dto.response.PreciseElectricitiesDto;
import live.ioteatime.frontservice.service.ElectricityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class IndexController {
    private final UserAdaptor userAdaptor;
    private final AdminAdaptor adminAdaptor;
    private final ElectricityAdaptor electricityAdaptor;
    private final ElectricityService electricityService;

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

        OrganizationResponse budget = userAdaptor.requestBudget().getBody();
        model.addAttribute("budget", budget);
        model.addAttribute("lastMonthKwh", electricityAdaptor.getLastMonthElectricity().getBody().getKwh());
        model.addAttribute("todayKwh", electricityAdaptor.getCurrentDayElectricity().getBody().getKwh());
        model.addAttribute("yesterdayKwh", electricityAdaptor.getLastDayElectricity().getBody().getKwh());
        model.addAttribute("thisMonthKwh", electricityAdaptor.getcurrentMonthElectricity().getBody().getKwh());
        model.addAttribute("wTop10", electricityService.getTop10Electricity());
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
        return electricityAdaptor.getMonthlyPredictedValues(LocalDateTime.now(), organizationId).getBody();
    }

    @GetMapping("/kwh")
    @ResponseBody
    public KwhDto getKwh() {
        long lastMonthKwh = electricityAdaptor.getLastMonthElectricity().getBody().getKwh();
        long thisMonthKwh = electricityAdaptor.getcurrentMonthElectricity().getBody().getKwh();
        long yesterdayKwh = electricityAdaptor.getLastDayElectricity().getBody().getKwh();
        long todayKwh = electricityAdaptor.getCurrentDayElectricity().getBody().getKwh();

        return new KwhDto(lastMonthKwh, thisMonthKwh, todayKwh, yesterdayKwh);
    }
}
