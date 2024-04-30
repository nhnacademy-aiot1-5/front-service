package live.ioteatime.frontservice.adaptor;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "gateway-service", contextId = "mqtt-sensor-adaptor")
public interface MqttSensorAdaptor {
}
