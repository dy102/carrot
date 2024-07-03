package app.feedback.common.exception;

public class CustomException extends RuntimeException {
    private final CustomErrorCode customErrorCode;

    public CustomException(CustomErrorCode customErrorCode) {
        this.customErrorCode = customErrorCode;
    }
}
