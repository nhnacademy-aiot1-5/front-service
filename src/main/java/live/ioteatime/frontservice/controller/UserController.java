package live.ioteatime.frontservice.controller;

import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.domain.Role;
import live.ioteatime.frontservice.dto.ChangePasswordRequest;
import live.ioteatime.frontservice.dto.GetUserResponse;
import live.ioteatime.frontservice.dto.OrganizationResponse;
import live.ioteatime.frontservice.dto.UpdateUserRequest;
import live.ioteatime.frontservice.utils.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Objects;

/**
 * GUEST, USER 유저 마이페이지 컨트롤러입니다.
 * @author 임세연
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String ACCESS_TOKEN_KEY = "iotaot";

    private final CookieUtil cookieUtil;
    private final UserAdaptor userAdaptor;

    /**
     *
     * @param request 브라우저 http 요청
     * @param model 권한에 따라서 GUEST에게는 userInfo, USER에게는 userInfo와 organizationInfo를 담아줍니다.
     * @return 마이페이지 뷰
     */
    @GetMapping("/mypage")
    public String userMypage(HttpServletRequest request, Model model){

        String accessToken = cookieUtil.getCookieValue(request, ACCESS_TOKEN_KEY);
        log.info("access token: {}", accessToken);

        if(Objects.isNull(accessToken)){
            return "redirect:/login";
        }

        GetUserResponse userInfo = userAdaptor.getUser(TOKEN_PREFIX + accessToken).getBody();
        model.addAttribute("userInfo", userInfo);
        log.info("userId={}, userRole={}", userInfo.getId(), userInfo.getRole());

        if(userInfo.getRole().equals(Role.GUEST)){
            return "/mypage/mypage";
        }

        OrganizationResponse organizationInfo = userAdaptor.getOrganization(TOKEN_PREFIX + accessToken).getBody();
        model.addAttribute("organizationInfo", organizationInfo);
        log.info("organization_name={}, electricity_montly_budget={}",organizationInfo.getName(), organizationInfo.getElectricityBudget());

        return "/mypage/mypage";

    }



    /**
     * 유저 정보를 수정합니다. name 필드만 수정 가능합니다.
     * @param request 브라우저 http 요청
     * @param updateUserRequest html 폼으로부터 받아온 요청 객체
     * @return 성공시, 마이페이지로 리다이렉트합니다.
     */
    @PostMapping("/update-user")
    public String updateUserInfo(HttpServletRequest request, @ModelAttribute UpdateUserRequest updateUserRequest){
        log.info("userName={}", updateUserRequest.getName());

        String accessToken = cookieUtil.getCookieValue(request, ACCESS_TOKEN_KEY);
        log.info("access token: {}", accessToken);
        if(Objects.isNull(accessToken)){
            return "redirect:/login";
        }

        userAdaptor.updateUser(TOKEN_PREFIX+accessToken, updateUserRequest);
        return "redirect:/mypage";
    }

    @PostMapping("/change-password")
    public String changePassword(HttpServletRequest request, RedirectAttributes redirectAttributes,
                                 @Valid @ModelAttribute ChangePasswordRequest changePasswordRequest, BindingResult bindingResult){

        log.info("기존={}, 새={}, 체크={}", changePasswordRequest.getCurrentPassword(), changePasswordRequest.getNewPassword(), changePasswordRequest.getPasswordCheck());

        if(bindingResult.hasFieldErrors()){
            redirectAttributes.addFlashAttribute("message", "모든 항목을 입력해주세요.");
            return "redirect:/change-password";
        }

        if(! changePasswordRequest.getNewPassword().equals(changePasswordRequest.getPasswordCheck())){
            redirectAttributes.addFlashAttribute("message", "비밀번호가 일치하지 않습니다.");
            return "redirect:/change-password";
        }

        String accessToken = cookieUtil.getCookieValue(request, ACCESS_TOKEN_KEY);
        log.info("access token: {}", accessToken);
        if(Objects.isNull(accessToken)){
            return "redirect:/login";
        }


        userAdaptor.updateUserPassword(TOKEN_PREFIX+accessToken, changePasswordRequest);
        redirectAttributes.addFlashAttribute("message", "비밀번호 변경이 완료되었습니다.");

        return "redirect:/mypage";
    }

    @GetMapping("/change-password")
    public String changePasswordPage(HttpServletRequest request){

        String accessToken = cookieUtil.getCookieValue(request, ACCESS_TOKEN_KEY);
        log.info("access token: {}", accessToken);
        if(Objects.isNull(accessToken)){
            return "redirect:/login";
        }

        return "/authentication/change-password";
    }

}
