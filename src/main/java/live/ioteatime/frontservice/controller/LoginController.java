package live.ioteatime.frontservice.controller;

import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.dto.LoginRequest;
import live.ioteatime.frontservice.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final UserAdaptor userAdaptor;
    private final String ACCESS_TOKEN_KEY = "iotaot";

    /**
     * 로그인 요청을 처리합니다.
     * @param loginRequest 사용자 입력 id, pw
     * @param response
     * @return 로그인 성공 시 메인 페이지로 리다이렉트
     */
    @PostMapping
    public String login(@ModelAttribute LoginRequest loginRequest, HttpServletResponse response){
        LoginResponse loginResponse = userAdaptor.login(loginRequest).getBody();

        log.info("loginResponse: {} {}", loginResponse.getType(), loginResponse.getToken());

        Cookie cookie = new Cookie(ACCESS_TOKEN_KEY, loginResponse.getToken());
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/";
    }

    @GetMapping
    public String login(){
        return "authentication/login";
    }

}
