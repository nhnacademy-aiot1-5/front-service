package live.ioteatime.frontservice.utils;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * 쿠키 처리를 위한 Util 클래스 입니다.
 */
@Component
public class CookieUtil {
    /**
     * 해당 HttpServletRequest에서 쿠키의 이름으로 쿠키 값을 검색 합니다.
     * @param req 쿠키 값을 검색할 HttpServletRequest 입니다.
     * @param name 검색할 쿠키의 이름 입니다.
     * @return 쿠키 값이 없는 경우 null을, 있는 경우 그 값을 반환 합니다.
     */
    public String getCookieValue(HttpServletRequest req, String name) {
        Cookie target = Optional.ofNullable(req.getCookies())
                .flatMap(cookies -> Arrays.stream(cookies)
                        .filter(c -> c.getName().equals(name))
                        .findFirst())
                .orElse(null);
        if(Objects.isNull(target)) return null;
        return target.getValue();
    }

}
