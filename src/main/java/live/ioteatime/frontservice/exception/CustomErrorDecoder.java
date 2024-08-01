package live.ioteatime.frontservice.exception;

import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 404 && "ElectricityAdaptor#getCurrentDayElectricity()".equals(methodKey)) {
            return null;
        }
        return defaultDecoder.decode(methodKey, response);
    }
}
