package app.feedback.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({CustomException.class})
    public ResponseEntity<CustomErrorResponse> handleDuplicateException(CustomException e, HttpServletRequest request) {
        log.info("errorCode : {}, url : {}, message : {}",
                e.getCustomErrorCode().getHttpStatus(), request.getRequestURI(), e.getMessage(), e);
        return new ResponseEntity<>(
                new CustomErrorResponse(
                        e.getCustomErrorCode().getHttpStatus().value(),
                        e.getMessage()
                ),
                e.getCustomErrorCode().getHttpStatus()
        );
    }
}