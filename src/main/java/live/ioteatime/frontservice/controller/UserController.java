package live.ioteatime.frontservice.controller;

import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.dto.GetUserResponse;
import live.ioteatime.frontservice.utils.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final String TOKEN_PREFIX = "Bearer ";
    private final String ACCESS_TOKEN_KEY = "iotaot";

    private final CookieUtil cookieUtil;
    private final UserAdaptor userAdaptor;

    @GetMapping("/mypage")
    public String userMypage(HttpServletRequest request, Model model){

        String accessToken = cookieUtil.getCookieValue(request, ACCESS_TOKEN_KEY);
        log.info("access token: {}", accessToken);

        if(Objects.isNull(accessToken)){
            return "redirect:/login";
        }

        GetUserResponse userInfo = userAdaptor.getUser(TOKEN_PREFIX + accessToken).getBody();
        model.addAttribute("userInfo", userInfo);
        log.info("userId={}, userRole={}", userInfo.getId(), userInfo.getRole());

        return "/mypage/mypage";

    }

    @PostMapping("/upgrade-request")
    public String upgradeFromGuestToMember(HttpServletRequest request, RedirectAttributes redirectAttributes){

        String accessToken = cookieUtil.getCookieValue(request, ACCESS_TOKEN_KEY);
        log.info("access token: {}", accessToken);
        if(Objects.isNull(accessToken)){
            return "redirect:/login";
        }

        userAdaptor.requestUpgradeToUser(TOKEN_PREFIX + accessToken);
        redirectAttributes.addFlashAttribute("message", "변경 요청이 완료되었습니다.");
        return "/mypage/mypage";
    }

}
