package app.feedback.auth.dto;


import app.feedback.member.domain.Member;
import app.feedback.member.domain.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record Authentication(
        String email,
        @Enumerated(value = EnumType.STRING) Role role
) {
    public static Authentication from(final Member member) {
        return new Authentication(member.getEmail(), member.getRole());
    }

    public void validateAdmin() {
        if (role != Role.ADMIN) {
            throw new IllegalArgumentException("관리자 권한이 필요합니다.");
        }
    }
}
