package app.feedback.common.exception;

public record CustomErrorResponse(
        int httpStatus,
        String message
) {
}