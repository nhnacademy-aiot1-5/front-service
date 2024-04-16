package live.ioteatime.frontservice.controller;

import live.ioteatime.frontservice.adaptor.AdminAdaptor;
import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.dto.GetUserResponse;
import live.ioteatime.frontservice.utils.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin")
public class AdminController {
    private final String ACCESS_TOKEN_KEY = "iotaot";
    private final String TOKEN_PREFIX = "Bearer ";
    private final AdminAdaptor adminAdaptor;
    private final UserAdaptor userAdaptor;
    private final CookieUtil cookieUtil;

    /**
     * 어드민 페이지에 처음 들어왔을때 기본으로 로딩되는 페이지로 GUEST 유저의 리스트를 확인할 수 있는 페이지를 로딩합니다.
     * @param request 페이지에서 받아온 요청들입니다.
     * @param model 결과 페이지에 전달할 파라미터입니다.
     * @return 해당하는 경로의 html 파일을 불러옵니다.
     */
    @GetMapping
    public String admin(HttpServletRequest request, Model model) {
        String accessToken = cookieUtil.getCookieValue(request, ACCESS_TOKEN_KEY);
        log.info("access token: {}", accessToken);
        if (Objects.isNull(accessToken)) {
            return "redirect:/login";
        }
        //사이드바 전용
        GetUserResponse userInfo = userAdaptor.getUser(TOKEN_PREFIX + accessToken).getBody();
        model.addAttribute("userInfo", userInfo);

        //Guest 유저 관리
        List<GetUserResponse> userList = adminAdaptor.requestGuestUsers(TOKEN_PREFIX + accessToken, 0).getBody();
        model.addAttribute("userList", userList);

        return "/admin/adminauthority";
    }

    /**
     *
     * GUEST 유저의 리스트를 확인할 수 있는 페이지를 로딩합니다.
     * @param request 페이지에서 받아온 요청들입니다.
     * @param model 결과 페이지에 전달할 파라미터입니다.
     * @return 해당하는 경로의 html 파일을 불러옵니다.
     */
    @GetMapping("/roleup")
    public String roleUp(HttpServletRequest request, Model model) {
        return admin(request, model);
    }

    @GetMapping("/users")
    public String userList(HttpServletRequest request, Model model) {
        String accessToken = cookieUtil.getCookieValue(request, ACCESS_TOKEN_KEY);
        log.info("access token: {}", accessToken);
        if (Objects.isNull(accessToken)) {
            return "redirect:/login";
        }

        GetUserResponse userInfo = userAdaptor.getUser("Bearer " + accessToken).getBody();
        model.addAttribute("userInfo", userInfo);
        log.info("userId: {}, userName: {}, userRole={}", userInfo.getId(), userInfo.getName(), userInfo.getRole());

        return "/admin/adminuser";
    }


//    /**
//     * GUEST 유저의 USER로의 권한 업그레이드 요청을 처리하는 핸들러 메서드입니다.
//     * @param request 브라우저 http 요청
//     * @param redirectAttributes 성공시, 성공 메시지를 담아줍니다.
//     * @return 마이페이지로 리다이렉트
//     */
//    @PostMapping("/upgrade-request")
//    public String upgradeFromGuestToMember(HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
//        String accessToken = cookieUtil.getCookieValue(request, ACCESS_TOKEN_KEY);
//        log.info("access token: {}", accessToken);
//        if (Objects.isNull(accessToken)) {
//            return "redirect:/login";
//        }
//
//        adminAdaptor.requestGuestUsers(TOKEN_PREFIX + accessToken);
//        redirectAttributes.addFlashAttribute("message", "변경 요청이 완료되었습니다.");
//        return "adminauthority";
//    }
}
