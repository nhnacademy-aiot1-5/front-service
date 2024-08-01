package live.ioteatime.frontservice.exception;

public class NotFoundException extends Exception {
    public NotFoundException(String resourceNotFound) {
        super(resourceNotFound);
    }
}
