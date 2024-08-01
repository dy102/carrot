package app.feedback.auth;


import app.feedback.auth.dto.Authentication;
import app.feedback.auth.dto.LoginRequest;
import app.feedback.common.exception.CustomException;
import app.feedback.member.domain.Member;
import app.feedback.member.domain.MemberRepository;
import app.feedback.member.domain.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static app.feedback.common.exception.CustomErrorCode.FORBIDDEN;
import static app.feedback.common.exception.CustomErrorCode.INCONSISTENCY;
import static app.feedback.common.exception.CustomErrorCode.UNAUTHORIZED;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication login(final LoginRequest loginRequest) {
        final Member member = memberRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new CustomException(INCONSISTENCY));
        member.checkPassword(loginRequest.password(), passwordEncoder);

        return Authentication.from(member);
    }

    @Override
    public void validateAdmin(final Authentication authentication) {
        validateUser(authentication);
        if (!authentication.isAdmin()) {
            throw new CustomException(FORBIDDEN);
        }
    }

    @Override
    public void validateAdminOrMe(final Authentication authentication,
                                  final String userId) {
        validateUser(authentication);
        if (!authentication.isAdmin() && !authentication.isMe(userId)) {
            log.info("email={}", authentication.email());
            throw new CustomException(FORBIDDEN);
        }
    }

    private boolean isLogin(Authentication authentication) {
        return authentication != null;
    }

    @Override
    public void validateUser(Authentication authentication) {
        if (!isLogin(authentication)) {
            throw new CustomException(UNAUTHORIZED);
        }
    }

}