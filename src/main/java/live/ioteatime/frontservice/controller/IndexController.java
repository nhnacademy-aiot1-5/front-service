package live.ioteatime.frontservice.controller;

import live.ioteatime.frontservice.adaptor.UserAdaptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class IndexController {

    private final String ACCESS_TOKEN_KEY = "iotaot";

    private final UserAdaptor userAdaptor;

    @GetMapping
    public String index(HttpServletRequest request, Model model) {
        Cookie[] cookies = request.getCookies();
        Cookie token = null;
        for (Cookie cookie : cookies) {
            if (ACCESS_TOKEN_KEY.equals(cookie.getName())) {
                token = cookie;
                break;
            }
        }
        String tokenValue = token.getValue();



        return "index";
    }
}
