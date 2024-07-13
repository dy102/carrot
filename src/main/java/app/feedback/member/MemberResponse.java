package app.feedback.member;

public record MemberResponse(
        String email,
        String name,
        String introduction
) {
}
