package live.ioteatime.frontservice.advice;

import feign.FeignException;
import live.ioteatime.frontservice.exception.UnauthorizedAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 전체적인 예외를 처리하는 핸들러 클래스입니다.
 * 예외 발생시 로그인 페이지로 리다이렉트 시키거나 에러 페이지를 리턴합니다.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 인증되지 않은 유저를 로그인 페이지로 리다이렉트 시킵니다.
     * @return 로그인 페이지로 리다이렉트 시킵니다.
     */
    @ExceptionHandler(UnauthorizedAccessException.class)
    public String handleUnauthorizedException() {
        return "redirect:/login";
    }

    /**
     * FeignException의 BadRequest 예외를 처리하는 핸들러 입니다.
     * @return 400 에러 페이지를 리턴합니다.
     */
    @ExceptionHandler(FeignException.BadRequest.class)
    public String handleBadRequest() {
        return "error/400";
    }

    /**
     * FeignException의 Unauthorized 예외를 처리하는 핸들러 입니다.
     * @return 401 에러 페이지를 리턴합니다.
     */
    @ExceptionHandler(FeignException.Unauthorized.class)
    public String handleFeignUnauthorizedException() {
        return "error/401";
    }

    /**
     * FeignException의 Forbidden 예외를 처리하는 핸들러 입니다.
     * @return 403 에러 페이지를 리턴합니다.
     */
    @ExceptionHandler(FeignException.Forbidden.class)
    public String handleFeignForbiddenException() {
        return "error/403";
    }

    /**
     * FeignException의 NotFound 예외를 처리하는 핸들러 입니다.
     * @return 404 에러 페이지를 리턴합니다.
     */
    @ExceptionHandler(FeignException.NotFound.class)
    public String handleFeignNotFoundException() {
        return "error/404";
    }

    /**
     * FeignException의 MethodNotAllowed 예외를 처리하는 핸들러 입니다.
     * @return 405 에러 페이지를 리턴합니다.
     */
    @ExceptionHandler(FeignException.MethodNotAllowed.class)
    public String handleFeignMethodNotAllowed() {
        return "error/405";
    }

    /**
     * FeignException의 InternalServerError 예외를 처리하는 핸들러 입니다.
     * @return 500 에러 페이지를 리턴합니다.
     */
    @ExceptionHandler(FeignException.InternalServerError.class)
    public String handleFeignInternalServerError() {
        return "error/500";
    }

    /**
     * FeignException의 ServiceUnavailable 예외를 처리하는 핸들러 입니다.
     * @return 503 에러 페이지를 리턴합니다.
     */
    @ExceptionHandler(FeignException.ServiceUnavailable.class)
    public String handleFeignServiceUnavailableException() {
        return "error/503";
    }
}

