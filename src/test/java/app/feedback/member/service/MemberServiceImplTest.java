package app.feedback.member.service;

import app.feedback.member.MemberCreateRequest;
import app.feedback.member.MemberResponse;
import app.feedback.member.MemberSimpleResponse;
import app.feedback.member.MemberUpdateRequest;
import app.feedback.member.MembersResponse;
import app.feedback.member.domain.Member;
import app.feedback.member.domain.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MemberServiceImplTest {
    @Autowired
    private MemberServiceImpl memberService;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void clear() {
        memberRepository.deleteAll();
    }

    @Test
    void 새로운_회원을_생성한다() {
        //given
        MemberCreateRequest memberCreateRequest
                = new MemberCreateRequest(
                "abc@gmail.com",
                "abc123",
                "에이비씨"
        );
        //when
        memberService.create(memberCreateRequest);
        //then
        Optional<Member> memberOptional = memberRepository.findByEmail("abc@gmail.com");
        assertThat(memberOptional).isNotNull();
    }

    @Test
    void 회원목록을_불러온다() {
        //given
        createSample();
        //when
        MembersResponse membersResponse = memberService.read();
        //then
        assertThat(membersResponse.memberResponses()).hasSize(3);
    }

    @Test
    void 회원정보_simple_을_불러온다() {
        //given
        createSample();
        //when
        MemberSimpleResponse response
                = memberService.findSimple("abc1@gmail.com");
        //then
        assertThat(response.name()).isEqualTo("에이비씨1");
        assertThat(response.introduction()).isNull();
    }

    @Test
    void 회원정보를_업데이트한다() {
        //given
        createSample();
        MemberUpdateRequest memberUpdateRequest
                = new MemberUpdateRequest(
                "abc123",
                "에이비씨321",
                "abcIntroduction"
        );
        //when
        String memberId = "abc1@gmail.com";
        memberService.update(memberId, memberUpdateRequest);
        MemberResponse memberResponse = memberService.find(memberId);
        //then
        assertThat(memberResponse.name()).isEqualTo("에이비씨321");
        assertThat(memberResponse.introduction()).isEqualTo("abcIntroduction");

    }

    private void createSample() {
        memberService.create(new MemberCreateRequest(
                "abc1@gmail.com",
                "abc123",
                "에이비씨1"
        ));
        memberService.create(new MemberCreateRequest(
                "abc2@gmail.com",
                "abc123",
                "에이비씨2"
        ));
        memberService.create(new MemberCreateRequest(
                "abc3@gmail.com",
                "abc123",
                "에이비씨3"
        ));
    }
}