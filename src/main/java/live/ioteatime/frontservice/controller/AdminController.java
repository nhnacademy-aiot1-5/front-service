package live.ioteatime.frontservice.controller;

import live.ioteatime.frontservice.adaptor.AdminAdaptor;
import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.dto.GetUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin")
public class AdminController {
    private final AdminAdaptor adminAdaptor;
    private final UserAdaptor userAdaptor;


    /**
     * 어드민 페이지에 처음 들어왔을때 기본으로 로딩되는 페이지로 GUEST 유저의 리스트를 확인할 수 있는 페이지를 로딩합니다.
     * @param model 결과 페이지에 전달할 파라미터입니다.
     * @return 해당하는 경로의 html 파일을 불러옵니다.
     */
    @GetMapping
    public String admin(Model model) {

        //사이드바 전용
        GetUserResponse userInfo = userAdaptor.getUser().getBody();
        model.addAttribute("userInfo", userInfo);

        //Guest 유저 관리
        List<GetUserResponse> userList = adminAdaptor.requestGuestUsers().getBody();
        model.addAttribute("userList", userList);

        return "/admin/adminauthority";
    }

    /**
     *
     * GUEST 유저의 리스트를 확인할 수 있는 페이지를 로딩합니다.
     * @param model 결과 페이지에 전달할 파라미터입니다.
     * @return 해당하는 경로의 html 파일을 불러옵니다.
     */
    @GetMapping("/roleup")
    public String roleUp(Model model) {
        return admin(model);
    }

    @GetMapping("/users")
    public String userList(Model model) {

        //사이드바
        GetUserResponse userInfo = userAdaptor.getUser().getBody();
        model.addAttribute("userInfo", userInfo);
        log.info("userId: {}, userName: {}, userRole={}", userInfo.getId(), userInfo.getName(), userInfo.getRole());

        //유저 정보
        List<GetUserResponse> userList = adminAdaptor.requestUsers().getBody();
        model.addAttribute("userList", userList);

        return "/admin/adminuser";
    }


    /**
     * GUEST 유저의 USER로의 권한 업그레이드 요청을 처리하는 핸들러 메서드입니다.
     * @return 마이페이지로 리다이렉트
     */
    @PutMapping("/roles/{userId}")
    public String upgradeFromGuestToMember(@PathVariable("userId") String userId, Model model) {

        //사이드바
        GetUserResponse userInfo = userAdaptor.getUser().getBody();
        model.addAttribute("userInfo", userInfo);
        log.info("userId: {}, userName: {}, userRole={}", userInfo.getId(), userInfo.getName(), userInfo.getRole());

        //유저정보 변경
        adminAdaptor.requestRole(userId);
        return "redirect:/admin";
    }
}
