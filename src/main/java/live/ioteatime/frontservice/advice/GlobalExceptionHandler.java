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

    @ExceptionHandler(FeignException.BadRequest.class)
    public String handleBadRequest() {
        return "error/400";
    }

    @ExceptionHandler(FeignException.Unauthorized.class)
    public String handleFeignUnauthorizedException() {
        return "error/401";
    }

    @ExceptionHandler(FeignException.Forbidden.class)
    public String handleFeignForbiddenException() {
        return "error/403";
    }

    @ExceptionHandler(FeignException.NotFound.class)
    public String handleFeignNotFoundException() {
        return "error/404";
    }

    @ExceptionHandler(FeignException.MethodNotAllowed.class)
    public String handleFeignMethodNotAllowed() {
        return "error/405";
    }

    @ExceptionHandler(FeignException.InternalServerError.class)
    public String handleFeignInternalServerError() {
        return "error/500";
    }

    @ExceptionHandler(FeignException.ServiceUnavailable.class)
    public String handleFeignServiceUnavailableException() {
        return "error/503";
    }
}

