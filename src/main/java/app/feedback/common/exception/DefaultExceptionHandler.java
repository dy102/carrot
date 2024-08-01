package app.feedback.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.nio.file.Files;

@Slf4j
@ControllerAdvice
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.info("errorCode : {}, url : {}, message : {}",
                status, request.getContextPath(), ex.getMessage());

        Resource resource = new ClassPathResource("templates/error/404.html");

        if (!resource.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("404 - Page Not Found");
        }

        try {
            String content = new String(Files.readAllBytes(resource.getFile().toPath()));
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_HTML)
                    .body(content);
        } catch (IOException e) {
            log.error("Error reading 404.html file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("500 - Internal Server Error");
        }
    }

    @ExceptionHandler({CustomException.class})
    public ResponseEntity<String> handleDuplicateException(CustomException e, HttpServletRequest request) {
        log.info("errorCode : {}, url : {}, message : {}",
                e.getCustomErrorCode().getHttpStatus(), request.getRequestURI(), e.getCustomErrorCode().getMessage(), e);
        return new ResponseEntity<>(
                e.getCustomErrorCode().getMessage(), e.getCustomErrorCode().getHttpStatus()
        );
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNotFoundResourceException(NotFoundException e, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error/404");
        log.info("errorCode : {}, url : {}, message : {}",
                e.getHttpStatus(), request.getRequestURI(), e.getMessage(), e);
        return modelAndView;
    }
}