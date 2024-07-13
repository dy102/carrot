package app.feedback.member;

import java.util.List;

public record MembersResponse(
        List<MemberResponse> memberResponses
) {
}
