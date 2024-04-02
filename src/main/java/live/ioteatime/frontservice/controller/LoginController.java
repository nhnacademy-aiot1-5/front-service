package live.ioteatime.frontservice.controller;

import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.dto.LoginRequest;
import live.ioteatime.frontservice.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
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

    @PostMapping
    public String login(@ModelAttribute LoginRequest loginRequest, HttpServletResponse response){
        LoginResponse loginResponse = userAdaptor.login(loginRequest).getBody();

        log.info("loginResponse: {} {}", loginResponse.getType(), loginResponse.getToken());

        Cookie cookie = new Cookie(HttpHeaders.AUTHORIZATION, loginResponse.getType() + "-" + loginResponse.getToken());
        cookie.setMaxAge(3600);
        response.addCookie(cookie);

        return "redirect:/";
    }

    @GetMapping
    public String login(){
        return "authentication/login";
    }

}
