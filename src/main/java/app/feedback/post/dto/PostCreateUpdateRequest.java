package app.feedback.post.dto;

public record PostCreateUpdateRequest(
        String title,
        String contents
) {
}
