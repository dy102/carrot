package app.feedback.member;

import app.feedback.auth.dto.Authentication;
import app.feedback.common.exception.CustomErrorCode;
import app.feedback.common.exception.CustomException;
import app.feedback.member.domain.Member;
import app.feedback.member.domain.MemberRepository;
import app.feedback.member.domain.PasswordEncoder;
import app.feedback.member.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationValidator authenticationValidator;

    @Override
    public void create(final MemberCreateRequest memberCreateRequest) {
        validateDuplicated(memberCreateRequest);
        Member member = new Member(
                memberCreateRequest.email(),
                memberCreateRequest.password(),
                memberCreateRequest.name(),
                passwordEncoder,
                Role.USER
        );
        memberRepository.save(member);
    }

    @Override
    public MembersResponse read(final Authentication authentication) {
        authenticationValidator.validateAdmin(authentication);
        List<Member> members = memberRepository.findAll();
        List<MemberResponse> memberResponses = new ArrayList<>();
        for (Member member : members) {
            MemberResponse memberResponse = new MemberResponse(
                    member.getEmail(), member.getName(), member.getIntroduction()
            );
            memberResponses.add(memberResponse);
        }
        return new MembersResponse(memberResponses);
    }

    @Override
    public MemberSimpleResponse findSimple(final Authentication authentication,
                                           final String memberId) {
        Optional<Member> memberOptional = memberRepository.findByEmail(memberId);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            return new MemberSimpleResponse(
                    member.getName(), member.getIntroduction()
            );
        } else {
            throw new CustomException(CustomErrorCode.INCONSISTENCY);
        }
    }

    @Override
    public void update(
            final Authentication authentication,
            final String memberId,
            final MemberUpdateRequest memberUpdateRequest
    ) {
        Optional<Member> memberOptional = memberRepository.findByEmail(memberId);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            member.update(
                    memberUpdateRequest.password(),
                    memberUpdateRequest.name(),
                    passwordEncoder
            );
            memberRepository.save(member);
        } else {
            throw new CustomException(CustomErrorCode.INCONSISTENCY);
        }
    }

    @Override
    public void delete(final Authentication authentication,
                       final String memberId) {
        authenticationValidator.validateAdminOrMe(authentication, memberId);
        Optional<Member> memberOptional = memberRepository.findByEmail(memberId);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            memberRepository.delete(member);
        } else {
            throw new CustomException(CustomErrorCode.INCONSISTENCY);
        }
    }


    @Override
    public MemberResponse find(final Authentication authentication,
                               final String memberId) {
        authenticationValidator.validateAdmin(authentication);
        Optional<Member> memberOptional = memberRepository.findByEmail(memberId);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            return new MemberResponse(
                    member.getEmail(), member.getName(), member.getIntroduction()
            );
        } else {
            throw new CustomException(CustomErrorCode.INCONSISTENCY);
        }
    }

    private void validateDuplicated(final MemberCreateRequest memberCreateRequest) {
        validateDuplicatedEmail(memberCreateRequest.email());
        validateDuplicatedName(memberCreateRequest.name());
    }

    private void validateDuplicatedEmail(final String email) {
        Optional<Member> memberOptionalByEmail = memberRepository.findByEmail(email);
        if (memberOptionalByEmail.isPresent()) {
            throw new CustomException(CustomErrorCode.DUPLICATED_EMAIL);
        }
    }

    private void validateDuplicatedName(final String name) {
        Optional<Member> memberOptionalByName = memberRepository.findByName(name);
        if (memberOptionalByName.isPresent()) {
            throw new CustomException(CustomErrorCode.DUPLICATED_NAME);
        }
    }

}
