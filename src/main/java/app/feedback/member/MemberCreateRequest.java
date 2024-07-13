package app.feedback.member;

public record MemberCreateRequest(
        String email,
        String password,
        String name
) {
}
