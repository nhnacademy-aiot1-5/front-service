package live.ioteatime.frontservice.adaptor;

import live.ioteatime.frontservice.dto.response.GetModbusSensorChannelResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "gateway-service", contextId = "channel-adaptor")
public interface ChannelAdaptor {
    @GetMapping("/api/sensor/modbus/channels")
    ResponseEntity<List<GetModbusSensorChannelResponse>> getChannels(@RequestParam int sensorId);
}
