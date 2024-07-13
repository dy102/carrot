package app.feedback.member;

public record MemberUpdateRequest(
        String password,
        String name
) {
}
