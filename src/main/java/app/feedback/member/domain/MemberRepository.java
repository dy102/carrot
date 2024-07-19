package app.feedback.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    @Query("select m from Member m where m.email=:email")
    Optional<Member> findByEmail(String email);

    Optional<Member> findByName(String name);
}
