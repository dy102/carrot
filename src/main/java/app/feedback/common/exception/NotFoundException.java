package app.feedback.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotFoundException extends RuntimeException {
    private final String message = "존재하지 않는 정보입니다.";
    private final HttpStatus httpStatus = HttpStatus.NOT_FOUND;


}
