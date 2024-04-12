package live.ioteatime.frontservice.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.dto.UserDto;
import live.ioteatime.frontservice.utils.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
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


        Cookie[] cookies = request.getCookies();
        Cookie token = null;
        for(Cookie cookie : cookies){
            if (cookie.getName().equals(ACCESS_TOKEN_KEY)){
                token = cookie;
                break;
            }
        }



        String accessToken = cookieUtil.getCookieValue(request, ACCESS_TOKEN_KEY);
        log.info("access token: {}", accessToken);

        if(Objects.isNull(accessToken)){
            return "redirect:/login";
        }

        // access token 까보고, userId 얻기
        // 임시 local test용 secret
        String secretKey = "asdasfndsjfgnsdhfgbjhdsfbgbhjawbjefbHsbfdjhadsbdfh1jfdbjhbfdjhbagdfj2h3bhj2b3jbhjbfjhbdsjhfbsjhdbfjhsbdfhjbsbdfhsbdfhsjdbfajhbsd";

        try {
            Jws<Claims> jwsClaims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(accessToken);
            String userId = jwsClaims.getBody().getSubject();
            log.info("userId={}", userId);

        UserDto userDto = userAdaptor.getUserInfo("Bearer "+accessToken, userId);
        model.addAttribute("loggedIn", userDto);

        } catch (SignatureException e){
            // todo
        }


        return "/mypage/mypage";

    }
}
