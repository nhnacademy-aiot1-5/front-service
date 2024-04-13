package live.ioteatime.frontservice.controller;

import feign.FeignException;
import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {

    private final UserAdaptor userAdaptor;

    @PostMapping
    public String register(@Valid RegisterRequest registerRequest,BindingResult bindingResult, RedirectAttributes redirectAttributes){

        if (bindingResult.hasFieldErrors()){
            redirectAttributes.addFlashAttribute("message", "모든 항목을 입력해주세요.");
            return "redirect:/register";
        }

        try {
            userAdaptor.createUser(registerRequest);
        }catch (FeignException.BadRequest exception){
            redirectAttributes.addFlashAttribute("message", "이미 존재하는 아이디입니다.");
            return "redirect:/register";
        }

        return "redirect:/login";
    }

    @GetMapping
    public String register(){
        return "authentication/register";
    }

}
