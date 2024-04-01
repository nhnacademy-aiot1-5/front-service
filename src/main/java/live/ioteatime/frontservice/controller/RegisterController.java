package live.ioteatime.frontservice.controller;

import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.dto.RegisterRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegisterController {

    UserAdaptor userAdaptor;

    @PostMapping
    public String register(@RequestBody RegisterRequest registerRequest){

        userAdaptor.createUser(registerRequest);

        return "index";
    }

}
