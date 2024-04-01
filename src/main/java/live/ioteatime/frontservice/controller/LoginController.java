package live.ioteatime.frontservice.controller;

import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final UserAdaptor userAdaptor;

    @PostMapping
    public String login(@RequestBody LoginRequest loginRequest) {

        userAdaptor.login(loginRequest);

        return "index";
    }

}
