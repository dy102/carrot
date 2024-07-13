package app.feedback.member.domain;


import app.feedback.common.exception.CustomException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

import static app.feedback.common.exception.CustomErrorCode.INCONSISTENCY;

@Component
public class PasswordEncoder {
    public String encode(final String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public void validateEquals(final String password, final String encoded) {
        final boolean equals = BCrypt.checkpw(password, encoded);
        if (!equals) {
            throw new CustomException(INCONSISTENCY);
        }
    }
}
