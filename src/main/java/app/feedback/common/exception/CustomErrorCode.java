package app.feedback.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CustomErrorCode {
    INCONSISTENCY(HttpStatus.BAD_REQUEST, "존재하지 않는 회원정보입니다."),
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    DUPLICATED_NAME(HttpStatus.CONFLICT, "이미 존재하는 이름입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인되지 않았습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근권한이 없습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 포스트입니다."),
    AI_NULL(HttpStatus.BAD_REQUEST, "AI 답글이 달리기 전에는 수정할 수 없습니다.");
    private final HttpStatus httpStatus;
    private final String message;

    CustomErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
