package live.ioteatime.frontservice.controller;

import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.dto.GetUserResponse;
import live.ioteatime.frontservice.utils.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Controller
@RequestMapping("/")
@Slf4j
@RequiredArgsConstructor
public class IndexController {

    private final CookieUtil cookieUtil;
    private final UserAdaptor userAdaptor;

    @GetMapping
    public String index(HttpServletRequest request, Model model) {

        String accessToken = cookieUtil.getCookieValue(request, "iotaot");
        log.info("access token: {}", accessToken);

        if(Objects.isNull(accessToken)){
            return "redirect:/login";
        }

        GetUserResponse userInfo = userAdaptor.getUser("Bearer "+accessToken).getBody();
        model.addAttribute("userInfo", userInfo);
        log.info("userId={}, userRole={}", userInfo.getId(), userInfo.getRole());

        return "index";
    }
}
