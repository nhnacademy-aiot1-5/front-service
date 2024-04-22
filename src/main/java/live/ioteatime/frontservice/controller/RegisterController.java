package live.ioteatime.frontservice.controller;

import feign.FeignException;
import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.dto.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * 회원가입 요청을 처리하는 컨트롤러입니다.
 * @author 임세연
 */
@Slf4j
@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {

    private final UserAdaptor userAdaptor;

    /**
     * 회원가입 요청을 처리하는 핸들러 메서드
     * @param registerRequest 폼으로부터 입력받은 회원가입 정보
     * @param bindingResult 폼 입력값 validation을 위해 사용
     * @param redirectAttributes 사용자 입력이 잘못되었을 경우, 에러 메시지를 담아 회원가입 페이지로 리다이렉트합니다.
     * @return 회원가입 성공시, 성공 메시지를 담아 로그인 페이지로 리다이렉트합니다.
     */
    @PostMapping
    public String register(@Valid RegisterRequest registerRequest,BindingResult bindingResult, RedirectAttributes redirectAttributes){

        if (bindingResult.hasFieldErrors()){
            redirectAttributes.addFlashAttribute("message", "모든 항목을 입력해주세요.");
            return "redirect:/register";
        }

        if(! registerRequest.getPassword().equals(registerRequest.getPasswordCheck())){
            redirectAttributes.addFlashAttribute("message", "비밀번호가 일치하지 않습니다.");
            return "redirect:/register";
        }

        try {
            userAdaptor.createUser(registerRequest);
        }catch (FeignException.BadRequest exception){
            redirectAttributes.addFlashAttribute("message", "이미 존재하는 아이디입니다.");
            return "redirect:/register";
        }

        redirectAttributes.addFlashAttribute("message", "IOTEATIME에 오신 것을 환영합니다.");
        return "redirect:/login";
    }

    /**
     *
     * @return 회원가입 페이지
     */
    @GetMapping
    public String register(){
        return "authentication/register";
    }

}
