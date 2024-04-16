package live.ioteatime.frontservice.advice;

import live.ioteatime.frontservice.exception.UnauthorizedAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedAccessException.class)
    public String handleUnauthorizedException() {
        return "redirect:/login";
    }
}

