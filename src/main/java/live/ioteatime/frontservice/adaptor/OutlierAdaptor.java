package live.ioteatime.frontservice.adaptor;

import live.ioteatime.frontservice.dto.OutlierDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "gateway-service", contextId = "outlier-adaptor")
public interface OutlierAdaptor {
    @GetMapping("/api/outlier")
    ResponseEntity<List<OutlierDto>> getOutliers(@RequestParam int organizationId);

    @PostMapping("/api/outlier")
    ResponseEntity<Integer> saveOutlier(@RequestBody OutlierDto outlierDto);

    @PutMapping("/api/outlier")
    ResponseEntity<Integer> updateOutlier(@RequestParam int id, @RequestParam int flag);

    @GetMapping("/control/sensor/on")
    void turnSensorOn(@RequestParam String sensorName, @RequestParam String devEui);
}
