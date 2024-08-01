package app.feedback.member.service;

import app.feedback.member.MemberCreateRequest;
import app.feedback.member.MemberResponse;
import app.feedback.member.MemberSimpleResponse;
import app.feedback.member.MemberUpdateRequest;
import app.feedback.member.MembersResponse;

public interface MemberService {
    void create(final MemberCreateRequest memberCreateRequest);

    MembersResponse read();

    MemberResponse find(final String memberId);

    MemberSimpleResponse findSimple(final String name);

    void update(final String memberId,
                final MemberUpdateRequest memberUpdateRequest);

    void delete(final String memberId);
}
