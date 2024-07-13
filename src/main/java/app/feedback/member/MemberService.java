package app.feedback.member;

import app.feedback.auth.dto.Authentication;

public interface MemberService {
    void create(final MemberCreateRequest memberCreateRequest);

    MembersResponse read(final Authentication authentication);

    MemberResponse find(final Authentication authentication, final String memberId);

    MemberSimpleResponse findSimple(final Authentication authentication, final String memberId);

    void update(final Authentication authentication, final String memberId,
                final MemberUpdateRequest memberUpdateRequest);

    void delete(final Authentication authentication, final String memberId);
}
