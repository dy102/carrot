package app.feedback.post.dto;

import java.util.List;

public record PostsResponse(
        List<PostSimpleDto> postSimpleRespons
) {
}
