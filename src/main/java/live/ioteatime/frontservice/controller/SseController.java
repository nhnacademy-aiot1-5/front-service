package live.ioteatime.frontservice.controller;

import live.ioteatime.frontservice.dto.Alert;
import live.ioteatime.frontservice.dto.response.GetUserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class SseController {
    // 조직 ID를 기준으로 Emitter 목록 관리
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

        System.out.println("클라이언트 구독됨: " + orgId + " 내의 " + userId);
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
                emitter.send(SseEmitter.event().data(alert.getMessage()));
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
}
