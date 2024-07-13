package app.feedback.member;

import app.feedback.auth.dto.Authentication;
import app.feedback.common.exception.CustomErrorCode;
import app.feedback.common.exception.CustomException;
import app.feedback.member.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationValidator {

    public void validateAdmin(final Authentication authentication) {
        if (!isAdmin(authentication)) {
            throw new CustomException(CustomErrorCode.FORBIDDEN);
        }
    }

    public void validateAdminOrMe(final Authentication authentication,
                                  final String memberId) {
        if (!isAdmin(authentication) && !isMe(authentication, memberId)) {
            throw new CustomException(CustomErrorCode.FORBIDDEN);
        }
    }

    private boolean isAdmin(final Authentication authentication) {
        return authentication.role().equals(Role.ADMIN);
    }

    private boolean isMe(final Authentication authentication,
                         final String memberId) {
        return authentication.email().equals(memberId);
    }
}
