package app.feedback.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "member")
@Getter
public class Member {
    @Id
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private String introduction;

    @Column(nullable = false)
    private String name;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    protected Member() {
    }

    public Member(final String email,
                  final String password,
                  final String name,
                  final PasswordEncoder passwordEncoder,
                  final Role role) {
        this.email = email;
        this.name = name;
        this.password = passwordEncoder.encode(password);
        this.role = role;
    }

    public void update(
            final String password,
            final String name,
            final String introduction,
            final PasswordEncoder passwordEncoder
    ) {
        this.name = name;
        this.introduction = introduction;
        this.password = passwordEncoder.encode(password);
    }

    public void checkPassword(final String password, final PasswordEncoder passwordEncoder) {
        passwordEncoder.validateEquals(password, this.password);
    }
}
