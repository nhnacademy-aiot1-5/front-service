package live.ioteatime.frontservice.interceptor;

import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.dto.response.GetUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class UserInfoInterceptor implements HandlerInterceptor {
    private final UserAdaptor userAdaptor;


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView model)
            throws Exception {
        if (!request.getMethod().equalsIgnoreCase("GET") || model == null) return;

        GetUserResponse userInfo = userAdaptor.getUser().getBody();
        if (userInfo == null) {
            throw new IllegalStateException();
        }
        model.addObject("userInfo", userInfo);
    }
}
