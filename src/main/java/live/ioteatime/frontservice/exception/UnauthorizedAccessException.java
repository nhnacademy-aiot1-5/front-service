package live.ioteatime.frontservice.exception;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String e) {
        super(e);
    }
}
