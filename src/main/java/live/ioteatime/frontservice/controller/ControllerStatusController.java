package live.ioteatime.frontservice.controller;

import live.ioteatime.frontservice.adaptor.ControllerStatusAdaptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class ControllerStatusController {
    private final ControllerStatusAdaptor controllerStatusAdaptor;
    @PutMapping("/controller-status/disable")
    public String outlierOff(HttpServletRequest request) {
        controllerStatusAdaptor.disableController("do_1");

        String referer = request.getHeader("Referer");
        if (referer != null && referer.length() > 0) {
            return "redirect:" + referer;
        }
        return "redirect:/";
    }
}
