package live.ioteatime.frontservice.controller;

import live.ioteatime.frontservice.adaptor.PlaceAdaptor;
import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.dto.response.GetPlaceResponse;
import live.ioteatime.frontservice.dto.response.GetUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/place")
public class PlaceController {
    private final UserAdaptor userAdaptor;
    private final PlaceAdaptor placeAdaptor;

    @GetMapping
    public String getPlaces(Model model) {
//사이드바 전용
        GetUserResponse userInfo = userAdaptor.getUser().getBody();
        model.addAttribute("userInfo", userInfo);
        log.info("userId: {}, userName: {}, userRole={}", userInfo.getId(), userInfo.getName(), userInfo.getRole());

        List<GetPlaceResponse> places = placeAdaptor.getPlaces(userInfo.getOrganization().getId()).getBody();
        model.addAttribute("places", places);
        return "/sensor/sensor-place";
    }
}
