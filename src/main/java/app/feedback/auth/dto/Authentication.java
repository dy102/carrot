package app.feedback.auth.dto;


import app.feedback.member.domain.Member;
import app.feedback.member.domain.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import static app.feedback.member.domain.Role.ADMIN;

public record Authentication(
        String email,
        @Enumerated(value = EnumType.STRING) Role role
) {
    public static Authentication from(final Member member) {
        return new Authentication(member.getEmail(), member.getRole());
    }

    public boolean isAdmin() {
        return this.role.equals(ADMIN);
    }

    public boolean isMe(final String userId) {
        return this.email.equals(userId);
    }
}
