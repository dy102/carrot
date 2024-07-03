package app.feedback.auth.dto;

public record LoginRequest(
        String email,
        String password,
        Boolean isAuto
) {
}
