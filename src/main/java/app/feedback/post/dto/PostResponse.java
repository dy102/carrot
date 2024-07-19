package app.feedback.post.dto;

import app.feedback.post.domain.Post;

import java.time.format.DateTimeFormatter;

public record PostResponse(
        String writerName,
        String writerId,
        String title,
        String contents,
        String image,
        String aiChat,
        String createdTime
) {
    public static PostResponse of(final Post post) {
        String aiChat = null;
        if (post.getAiChat() != null) {
            aiChat = post.getAiChat().getChat();
        }
        return new PostResponse(
                post.getWriter().getName(), post.getWriter().getEmail(),
                post.getTitle(), post.getContents(), post.getImage(),
                aiChat, post.getCreatedTime().format(DateTimeFormatter.ISO_LOCAL_DATE)
        );
    }
}
