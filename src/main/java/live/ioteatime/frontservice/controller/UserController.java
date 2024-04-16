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

/**
 * GUEST, USER 유저 마이페이지 컨트롤러입니다.
 * @author 임세연
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final String TOKEN_PREFIX = "Bearer ";
    private final String ACCESS_TOKEN_KEY = "iotaot";

    private final CookieUtil cookieUtil;
    private final UserAdaptor userAdaptor;

    /**
     *
     * @param request 브라우저 http 요청
     * @param model 권한에 따라서 GUEST에게는 userInfo, USER에게는 userInfo와 organizationInfo를 담아줍니다.
     * @return 마이페이지 뷰
     */
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



}
