package live.ioteatime.frontservice.advice;

import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.dto.GetUserResponse;
import live.ioteatime.frontservice.utils.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@RequiredArgsConstructor
public class UserControllerAdvice {

    private final UserAdaptor userAdaptor;
    private final CookieUtil cookieUtil;

    @ModelAttribute
    public void addUserToModel(HttpServletRequest request, Model model) {
        String accessToken = cookieUtil.getCookieValue(request, "iotaot");
        if (accessToken != null) {
            GetUserResponse userInfo = userAdaptor.getUser("Bearer " + accessToken).getBody();
            model.addAttribute("userInfo", userInfo);
        }
    }
}
