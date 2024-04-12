package live.ioteatime.frontservice.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.dto.GetUserResponse;
import live.ioteatime.frontservice.utils.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

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

        GetUserResponse userInfo = userAdaptor.getUser("Bearer "+accessToken).getBody();
        model.addAttribute("userInfo", userInfo);
        log.info("userId={}, userRole={}", userInfo.getId(), userInfo.getRole());

        return "/mypage/mypage";

    }
}
