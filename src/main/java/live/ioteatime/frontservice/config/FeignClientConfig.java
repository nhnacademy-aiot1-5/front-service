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

/**
 * FeignClient에 관한 설정을 처리하는 클래스 입니다.
 */
@Slf4j
@Configuration
public class FeignClientConfig {

    /**
     * 로그인 하지 않은 유저가 엑세스 토큰 없이 회원 가입 페이지와 로그인 페이지에 접속할 수 있도록 하고,
     * 회원 가입과 로그인을 제외한 요청이 들어올 경우 엑세스 토큰이 없다면 UnauthorizedAccessException을 리턴하고,
     * 토큰이 있다면 헤더에 토큰 정보를 저장 합니다.
     *
     * @param cookieUtil 엑세스 토큰을 가져오기위해 사용 됩니다.
     * @return 로그인이나 회원 가입 요청이라면 해당 페이지를, 그 외의 요청에서 토큰이 없는 경우 에러를 던집니다.
     */
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

