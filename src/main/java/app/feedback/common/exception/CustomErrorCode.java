package app.feedback.common.exception;

import org.springframework.http.HttpStatus;

public enum CustomErrorCode {
    INCONSISTENCY(HttpStatus.BAD_REQUEST, "존재하지 않는 회원정보입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인되지 않았습니다.");
    private final HttpStatus httpStatus;
    private final String message;

    CustomErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
