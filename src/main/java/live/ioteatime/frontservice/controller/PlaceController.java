package live.ioteatime.frontservice.controller;

import live.ioteatime.frontservice.adaptor.PlaceAdaptor;
import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.dto.PlaceDto;
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
    public static final String REDIRECT_PLACES = "redirect:/places";
    private final UserAdaptor userAdaptor;
    private final PlaceAdaptor placeAdaptor;

    @GetMapping
    public String getPlaces(Model model) {
        GetUserResponse userInfo = userAdaptor.getUser().getBody();
        assert userInfo != null;
        List<PlaceDto> placeList = placeAdaptor.getPlacesByOrganizationId(userInfo.getOrganization().getId()).getBody();
        model.addAttribute("placeList", placeList);
        return "sensor/sensor-place";
    }

    @PostMapping
    public String createPlace(@ModelAttribute PlaceDto placeDto) {
        placeAdaptor.createPlace(placeDto);
        return REDIRECT_PLACES;
    }

    @PutMapping("/{placeId}")
    public String updatePlace(@PathVariable("placeId") int placeId, @RequestParam String placeName){
        placeAdaptor.updatePlace(placeId, placeName);
        return REDIRECT_PLACES;
    }

    @DeleteMapping("/{placeId}")
    public String deletePlace(@PathVariable("placeId") int placeId){
         placeAdaptor.deletePlace(placeId);
        return REDIRECT_PLACES;
    }
}
