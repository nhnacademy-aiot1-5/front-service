package live.ioteatime.frontservice.config;

import feign.RequestInterceptor;
import live.ioteatime.frontservice.exception.UnauthorizedAccessException;
import live.ioteatime.frontservice.utils.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestTokenBearerInterceptor(CookieUtil cookieUtil) {
        return requestTemplate -> {

            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

            String url = requestTemplate.url();
            log.error("{} {}", url, request.getMethod());
            if (url.equals("/auth/login") ||
                    (request.getMethod().equals("GET") && (url.equals("/users/update-user") || url.equals("/users/change-password"))) ||
                    (url.equals("/api/users") && request.getMethod().equals("POST"))
            ) {
                return;
            }
            String accessToken = cookieUtil.getCookieValue(request, "iotaot");
            if (accessToken == null) {
                throw new UnauthorizedAccessException("Access token is missing");
            }
            requestTemplate.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
            log.info("AccessToken applied for request: {}", requestTemplate.headers().get("Authorization"));

        };
    }
}

