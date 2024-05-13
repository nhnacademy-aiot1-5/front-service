package live.ioteatime.frontservice.utils;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Component
public class CookieUtil {
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
