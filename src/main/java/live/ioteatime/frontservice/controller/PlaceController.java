package live.ioteatime.frontservice.controller;

import live.ioteatime.frontservice.adaptor.PlaceAdaptor;
import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.dto.PlaceDto;
import live.ioteatime.frontservice.dto.request.PlaceRequest;
import live.ioteatime.frontservice.dto.response.GetUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/places")
public class PlaceController {
    private final UserAdaptor userAdaptor;
    private final PlaceAdaptor placeAdaptor;

    @GetMapping
    public String getPlaces(Model model) {
        GetUserResponse userInfo = userAdaptor.getUser().getBody();
        model.addAttribute("userInfo", userInfo);

        List<PlaceDto> placeList = placeAdaptor.getPlaces(userInfo.getOrganization().getId()).getBody();
        model.addAttribute("placeList", placeList);
        return "/sensor/sensor-place";
    }

    @PostMapping
    public String createPlace(@ModelAttribute PlaceRequest placeRequest) {
        placeAdaptor.createPlace(placeRequest);
        return "redirect:/places";
    }

    @PutMapping("/{placeId}")
    public String updatePlace(@PathVariable("placeId") int placeId, @RequestParam String placeName){
        placeAdaptor.updatePlace(placeId, placeName);
        return "redirect:/places";
    }

    @DeleteMapping("/{placeId}")
    public String deletePlace(@PathVariable("placeId") int placeId){
         placeAdaptor.deletePlace(placeId);
        return "redirect:/places";
    }
}
