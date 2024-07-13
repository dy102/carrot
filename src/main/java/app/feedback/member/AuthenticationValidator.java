package app.feedback.member;

import app.feedback.auth.dto.Authentication;
import app.feedback.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static app.feedback.common.exception.CustomErrorCode.FORBIDDEN;
import static app.feedback.member.domain.Role.ADMIN;

@Component
@RequiredArgsConstructor
public class AuthenticationValidator {

    public void validateAdmin(final Authentication authentication) {
        if (!isAdmin(authentication)) {
            throw new CustomException(FORBIDDEN);
        }
    }

    public void validateAdminOrMe(final Authentication authentication,
                                  final String memberId) {
        if (!isAdmin(authentication) && !isMe(authentication, memberId)) {
            throw new CustomException(FORBIDDEN);
        }
    }

    private boolean isAdmin(final Authentication authentication) {
        return authentication.role().equals(ADMIN);
    }

    private boolean isMe(final Authentication authentication,
                         final String memberId) {
        return authentication.email().equals(memberId);
    }
}
