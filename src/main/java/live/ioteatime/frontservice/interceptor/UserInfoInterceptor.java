package live.ioteatime.frontservice.interceptor;

import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.dto.response.GetUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 유저 정보에 관한 처리를 하는 interceptor 클래스 입니다.
 */
@Slf4j
@RequiredArgsConstructor
public class UserInfoInterceptor implements HandlerInterceptor {
    private final UserAdaptor userAdaptor;

    /**
     * 사이드 바에 필요한 객체를 모델에 넣어주는 인터셉터 입니다.
     * @param request
     * @param response
     * @param handler
     * @param model 유저 정보가 담긴 모델입니다.
     * @throws Exception 유저 정보가 없으면 IllegalStateException을 던집니다.
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView model)
            throws Exception {

        if (!request.getMethod().equalsIgnoreCase("GET") || model == null) return;
        log.info("interceptor 실행 됨");
        GetUserResponse userInfo = userAdaptor.getUser().getBody();
        if (userInfo == null) {
            throw new IllegalStateException();
        }
        model.addObject("userInfo", userInfo);
    }
}
