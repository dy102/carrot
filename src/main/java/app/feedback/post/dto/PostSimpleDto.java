package app.feedback.post.dto;

import app.feedback.post.domain.Post;

public record PostSimpleDto(
        Long id,
        String title,
        String writerName,
        String image
) {
    public static PostSimpleDto of(Post post) {
        return new PostSimpleDto(
                post.getId(), post.getTitle(), post.getWriter().getName(), post.getImage()
        );
    }
}
