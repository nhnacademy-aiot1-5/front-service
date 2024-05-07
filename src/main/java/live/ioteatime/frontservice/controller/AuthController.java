package live.ioteatime.frontservice.controller;

import feign.FeignException;
import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.dto.request.LoginRequest;
import live.ioteatime.frontservice.dto.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserAdaptor userAdaptor;
    private final String ACCESS_TOKEN_KEY = "iotaot";

    /**
     * 로그인 요청을 처리합니다.
     * @param loginRequest 사용자 입력 id, pw
     * @param response
     * @return 로그인 성공 시 메인 페이지로 리다이렉트
     */
    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest loginRequest, HttpServletResponse response, RedirectAttributes redirectAttributes){
        try {
            LoginResponse loginResponse = userAdaptor.login(loginRequest).getBody();

            log.info("loginResponse: {} {}", loginResponse.getType(), loginResponse.getToken());

            Cookie cookie = new Cookie(ACCESS_TOKEN_KEY, loginResponse.getToken());
            cookie.setMaxAge(3600);
            response.addCookie(cookie);

        } catch (FeignException.Unauthorized exception) {
            redirectAttributes.addFlashAttribute("message", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "redirect:/login";
        }


        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request){
        Cookie cookie = WebUtils.getCookie(request, "iotaot");
        if (cookie != null) {
            return "redirect:/";
        }
        return "authentication/login";
    }

    /**
     * 로그아웃 요청을 처리합니다.
     * @param response
     * @return 로그아웃시 메인 페이지로 리다이렉트합니다.
     */
    @GetMapping("/logout")
    public String logout(HttpServletResponse response){
        Cookie cookie = new Cookie(ACCESS_TOKEN_KEY, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/";
    }

}
