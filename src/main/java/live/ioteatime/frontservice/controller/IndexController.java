package live.ioteatime.frontservice.controller;

import live.ioteatime.frontservice.adaptor.AdminAdaptor;
import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.domain.Role;
import live.ioteatime.frontservice.dto.response.GetUserResponse;
import live.ioteatime.frontservice.dto.response.OrganizationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class IndexController {
    private final UserAdaptor userAdaptor;
    private final AdminAdaptor adminAdaptor;

    @GetMapping
    public String index(Model model) {
        GetUserResponse userInfo = userAdaptor.getUser().getBody();
        model.addAttribute("userInfo", userInfo);

        if (userInfo.getRole() == Role.GUEST) {
            return "redirect:/mypage";
        }

        //조직 예산
        OrganizationResponse budget = userAdaptor.requestBudget().getBody();
        model.addAttribute("budget", budget);
        return "index";
    }

    @PutMapping("/budget")
    public String updateBudget(@RequestParam Long budget, Model model) {

        //목표금액 변경
        adminAdaptor.updateBudget(budget);
        return "redirect:/";
    }
}
