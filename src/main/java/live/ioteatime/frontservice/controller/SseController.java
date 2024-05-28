package live.ioteatime.frontservice.controller;

import live.ioteatime.frontservice.dto.Alert;
import live.ioteatime.frontservice.dto.OutlierRequest;
import live.ioteatime.frontservice.dto.response.GetUserResponse;
import live.ioteatime.frontservice.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
public class SseController {
    private final SseService sseService;
    private final Map<Integer, Map<String, SseEmitter>> emitters = new ConcurrentHashMap<>();

    @GetMapping("/sse")
    public SseEmitter subscribeSse(HttpServletRequest request) {
        GetUserResponse userInfo = (GetUserResponse) request.getAttribute("userInfo");
        int orgId = userInfo.getOrganization().getId();
        String userId = userInfo.getId();

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        this.emitters.computeIfAbsent(orgId, k -> new ConcurrentHashMap<>()).put(userId, emitter);

        emitter.onCompletion(() -> this.emitters.get(orgId).remove(userId));
        emitter.onTimeout(() -> this.emitters.get(orgId).remove(userId));
        return emitter;
    }

    @PostMapping("/notify")
    public void notifyClients(@RequestBody Alert alert) {
        int targetOrgId = alert.getOrganizationId();
        List<String> toRemove = new ArrayList<>();
        Map<String, SseEmitter> targetEmitters = emitters.getOrDefault(targetOrgId, new ConcurrentHashMap<>());

        for (Map.Entry<String, SseEmitter> entry : targetEmitters.entrySet()) {
            SseEmitter emitter = entry.getValue();
            try {
                emitter.send(SseEmitter.event().data(sseService.getMessage(alert)));
            } catch (IOException e) {
                toRemove.add(entry.getKey());
                emitter.completeWithError(e);
            }
        }

        toRemove.forEach(targetEmitters::remove);
        if (targetEmitters.isEmpty()) {
            emitters.remove(targetOrgId);
        }
    }

    @PostMapping("/outlier-off")
    public ResponseEntity<?> turnOnLight(@RequestBody OutlierRequest outlierId) {
        sseService.turnSensorOn("light", "24E124445D189958");
        sseService.updateOutlier(outlierId.getOutlierId(), 1);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Outlier resolved successfully");
        response.put("outlierId", outlierId.getOutlierId());
        return ResponseEntity.ok(response);
    }
}
