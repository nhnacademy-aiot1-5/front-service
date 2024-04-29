package live.ioteatime.frontservice.advice;

import feign.FeignException;
import live.ioteatime.frontservice.exception.UnauthorizedAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedAccessException.class)
    public String handleUnauthorizedException() {
        return "redirect:/login";
    }


    @ExceptionHandler(FeignException.Unauthorized.class)
    public String handleFeignUnauthorizedException() {
        return "error/401";
    }

    @ExceptionHandler(FeignException.MethodNotAllowed.class)
    public String handleFeignMethodNotAllowed() {
        return "error/405";
    }


}

